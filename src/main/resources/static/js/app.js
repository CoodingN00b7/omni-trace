// ── Email Checker ─────────────────────────────────────────────────────────────

async function checkEmail() {
    const input  = document.getElementById('emailInput');
    const btn    = document.getElementById('checkBtn');
    const result = document.getElementById('emailResult');
    const email  = input.value.trim();
    if (!email) { input.focus(); return; }

    btn.disabled = true;
    btn.innerHTML = '<span class="spinner"></span> Checking...';
    result.innerHTML = '';

    try {
        const res  = await fetch(`/api/email/check?email=${encodeURIComponent(email)}`);
        const data = await res.json();
        renderBreachResult(data, result);
    } catch (e) {
        result.innerHTML = `<div class="alert alert-error">&#9888; Check failed: ${e.message}</div>`;
    } finally {
        btn.disabled = false;
        btn.innerHTML = '&#128269; Scan Now';
    }
}

function renderBreachResult(data, container) {
    const cls   = data.highRisk ? 'highrisk' : data.breached ? 'breached' : 'clean';
    const icon  = data.highRisk ? '&#128680;' : data.breached ? '&#9888;' : '&#10003;';
    const title = data.highRisk ? 'High Risk — Seriously Exposed!'
                : data.breached  ? 'Breached — Found in Database'
                : 'Good News — No Breaches Found!';
    const color = data.highRisk ? 'var(--red)' : data.breached ? 'var(--orange)' : 'var(--green)';
    const risk  = data.highRisk ? 'HIGH' : data.breached ? 'MEDIUM' : 'LOW';

    const sourcesHtml = data.sources && data.sources.length > 0
        ? `<div class="breach-sources">
               <div class="source-label">&#128203; Appeared in these breaches</div>
               <div class="source-chips">
                   ${data.sources.map(s => `<span class="source-chip">${s}</span>`).join('')}
               </div>
           </div>`
        : data.breached
        ? `<div class="breach-sources">
               <p style="font-size:12px;color:var(--text3)">
                   &#128273; Source details require a paid LeakCheck key.
               </p>
           </div>`
        : '';

    container.innerHTML = `
        <div class="breach-result ${cls}">
            <div class="breach-header">
                <div class="breach-icon">${icon}</div>
                <div>
                    <div class="breach-title">${title}</div>
                    <div class="breach-email">${data.email}</div>
                </div>
            </div>
            <div class="breach-stats">
                <div class="breach-stat">
                    <div class="breach-stat-label">Breaches found</div>
                    <div class="breach-stat-val" style="color:${data.found > 0 ? 'var(--red)' : 'var(--green)'}">${data.found}</div>
                </div>
                <div class="breach-stat">
                    <div class="breach-stat-label">Risk level</div>
                    <div class="breach-stat-val" style="color:${color}">${risk}</div>
                </div>
            </div>
            ${sourcesHtml}
        </div>`;
}

// ── Scanner ───────────────────────────────────────────────────────────────────

function initScanner() {
    const dz = document.getElementById('dropzone');
    const fi = document.getElementById('fileInput');
    if (!dz) return;

    dz.addEventListener('click', () => fi.click());
    dz.addEventListener('dragover',  e => { e.preventDefault(); dz.classList.add('dragover'); });
    dz.addEventListener('dragleave', () => dz.classList.remove('dragover'));
    dz.addEventListener('drop', e => {
        e.preventDefault(); dz.classList.remove('dragover');
        if (e.dataTransfer.files[0]) setFile(e.dataTransfer.files[0]);
    });
    fi.addEventListener('change', () => { if (fi.files[0]) setFile(fi.files[0]); });
}

function setFile(file) {
    const dz = document.getElementById('dropzone');
    dz.innerHTML = `
        <div class="dz-icon">&#128196;</div>
        <div class="dz-title">${file.name}</div>
        <div class="dz-sub">${(file.size / 1024).toFixed(1)} KB &mdash; click to change</div>`;
    const dt = new DataTransfer();
    dt.items.add(file);
    document.getElementById('fileInput').files = dt.files;
}

function switchTab(t) {
    ['file','text'].forEach(id => {
        document.getElementById('tab-' + id).classList.toggle('active', id === t);
        document.getElementById('pane-' + id).style.display = id === t ? 'block' : 'none';
    });
}

async function runScan() {
    const btn    = document.getElementById('scanBtn');
    const res    = document.getElementById('scanResult');
    const isFile = document.getElementById('tab-file').classList.contains('active');

    btn.disabled = true;
    btn.innerHTML = '<span class="spinner"></span> Scanning...';
    res.innerHTML = '';

    try {
        let data;
        if (isFile) {
            const f = document.getElementById('fileInput').files[0];
            if (!f) { alert('Please select a file.'); return; }
            const fd = new FormData();
            fd.append('file', f);
            data = await (await fetch('/api/scan/upload', { method: 'POST', body: fd })).json();
        } else {
            const text = document.getElementById('scanText').value.trim();
            if (!text) { alert('Please enter text to scan.'); return; }
            data = await (await fetch('/api/scan/text', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ text, label: 'web-paste' })
            })).json();
        }
        renderScanResult(data, res);
    } catch (e) {
        res.innerHTML = `<div class="alert alert-error">&#9888; ${e.message}</div>`;
    } finally {
        btn.disabled = false;
        btn.innerHTML = '&#128269; Run Scan';
    }
}

function renderScanResult(data, container) {
    if (!data.success) {
        container.innerHTML = `<div class="alert alert-error">&#9888; ${data.error}</div>`;
        return;
    }

    const RCOLORS = { 10:'var(--red)', 7:'var(--orange)', 6:'var(--amber)', 4:'var(--cyan)', 2:'var(--green)' };

    let breachHtml = '';
    if (data.breachResults && data.breachResults.length > 0) {
        const rows = data.breachResults.map(br => `
            <div style="display:flex;align-items:center;gap:12px;padding:12px 0;border-bottom:1px solid var(--border)">
                <span style="font-size:16px">${br.breached ? '&#9888;' : '&#10003;'}</span>
                <span style="font-family:'JetBrains Mono',monospace;font-size:13px;color:var(--text)">${br.email}</span>
                <span style="margin-left:auto;font-size:12px;font-weight:700;color:${br.highRisk?'var(--red)':br.breached?'var(--orange)':'var(--green)'}">
                    ${br.breached ? br.found + ' breach(es)' : 'Clean'}
                </span>
            </div>`).join('');
        breachHtml = `
            <div class="card" style="margin-bottom:16px">
                <div class="card-header"><span class="card-title">&#9993; Email Breach Results</span></div>
                <div class="card-body" style="padding:0 20px">${rows}</div>
            </div>`;
    }

    const rows = (data.findings || []).map(f => `
        <tr class="${f.flagged ? 'flagged-row' : ''}">
            <td><span class="badge badge-type">${f.dataType}</span></td>
            <td class="mono">${f.maskedValue || '&mdash;'}</td>
            <td><span class="${'risk-'+f.riskScore}">${f.riskScore}</span></td>
            <td>${f.flagged
                ? '<span class="badge badge-high">HIGH RISK</span>'
                : '<span style="color:var(--text3);">&mdash;</span>'}</td>
        </tr>`).join('') || `<tr><td colspan="4" style="text-align:center;color:var(--text3);padding:30px">No PII found</td></tr>`;

    container.innerHTML = `
        <div class="scan-result">
            <div class="stat-grid" style="margin-bottom:16px">
                <div class="stat-card indigo" style="padding:16px 18px">
                    <div class="stat-label">Total Found</div>
                    <div class="stat-value" style="font-size:26px">${data.totalFindings}</div>
                </div>
                <div class="stat-card red" style="padding:16px 18px">
                    <div class="stat-label">High-Risk</div>
                    <div class="stat-value" style="font-size:26px">${data.flagged}</div>
                </div>
                <div class="stat-card green" style="padding:16px 18px">
                    <div class="stat-label">Types</div>
                    <div class="stat-value" style="font-size:26px">${Object.keys(data.breakdown||{}).length}</div>
                </div>
            </div>
            ${breachHtml}
            <div class="card">
                <div class="card-header"><span class="card-title">&#128202; Findings</span></div>
                <div class="table-wrap">
                    <table>
                        <thead><tr>
                            <th>Type</th><th>Masked Value</th><th>Risk</th><th>Flag</th>
                        </tr></thead>
                        <tbody>${rows}</tbody>
                    </table>
                </div>
            </div>
        </div>`;
}

// ── Records ───────────────────────────────────────────────────────────────────

function filterRecords(type) {
    document.querySelectorAll('.pill').forEach(p =>
        p.classList.toggle('active', p.dataset.type === type));
    document.querySelectorAll('tbody tr[data-type]').forEach(tr =>
        tr.style.display = (type === 'ALL' || tr.dataset.type === type) ? '' : 'none');
}

async function deleteRecord(id, btn) {
    if (!confirm('Delete this record?')) return;
    await fetch(`/api/records/${id}`, { method: 'DELETE' });
    btn.closest('tr').remove();
}

async function clearAll() {
    if (!confirm('Delete ALL records? This cannot be undone.')) return;
    await fetch('/api/records', { method: 'DELETE' });
    window.location.reload();
}

// ── Init ──────────────────────────────────────────────────────────────────────

document.addEventListener('DOMContentLoaded', () => {
    initScanner();

    // Enter key on email inputs
    ['emailInput'].forEach(id => {
        const el = document.getElementById(id);
        if (el) el.addEventListener('keydown', e => e.key === 'Enter' && checkEmail());
    });

    // Animate stat values counting up
    document.querySelectorAll('.stat-value[data-target]').forEach(el => {
        const target = parseInt(el.dataset.target, 10);
        if (isNaN(target)) return;
        let cur = 0;
        const step = Math.ceil(target / 30);
        const timer = setInterval(() => {
            cur = Math.min(cur + step, target);
            el.textContent = cur;
            if (cur >= target) clearInterval(timer);
        }, 30);
    });

    // Animate breakdown bars
    setTimeout(() => {
        document.querySelectorAll('.breakdown-bar[data-width]').forEach(bar => {
            bar.style.width = bar.dataset.width + '%';
        });
    }, 100);
});
