const CONFIG = {
    API: 'http://localhost:8080/api',
    SESSION: 'unifebe'
};

// ======== GERENCIAMENTO DE SESSÃO ========
const Session = {
    save(u) { 
        sessionStorage.setItem(CONFIG.SESSION, JSON.stringify(u)); 
    },
    get() { 
        const d = sessionStorage.getItem(CONFIG.SESSION); 
        return d ? JSON.parse(d) : null; 
    },
    clear() { 
        sessionStorage.removeItem(CONFIG.SESSION); 
    },
    Admin() {
        const u = this.get(); 
        return u && u.tipo === 'A'; 
    },
    isLogged() {
        return this.get() !== null; 
    }
};

// ======== MENU LATERAL ========
function toggleMenu() {
    const menu = document.getElementById("side-menu");
    const overlay = document.getElementById("menuOverlay");
    
    if (menu.style.width === "280px") {
        menu.style.width = "0";
        if (overlay) overlay.classList.remove("show");
    } else {
        menu.style.width = "280px";
        if (overlay) overlay.classList.add("show");
    }
}

function toggleSubmenu(element) {
    document.querySelectorAll('.menu-admin.open').forEach(item => {
        if (item !== element) {
            item.classList.remove('open');
        }
    });
    element.classList.toggle('open');
}

// ======== UI CONTROLLER ========
const UIController = {
    init() {
        this.updateHeader();
        this.updateMenu();
        this.updateIndexCards();
    },
    
    updateHeader() {
        const user = Session.get();
        const isLogged = Session.isLogged();
        const isAdmin = Session.isAdmin();
        
        const userWelcome = document.getElementById('userWelcome');
        const headerUserName = document.getElementById('headerUserName');
        const headerUserRole = document.getElementById('headerUserRole');
        const btnPerfil = document.getElementById('btnPerfil');
        const btnLogout = document.getElementById('btnLogout');
        const btnLoginHeader = document.getElementById('btnLoginHeader');
        
        if (isLogged && user) {
            // Mostrar nome do usuário para admin
            if (isAdmin) {
                if (userWelcome) userWelcome.style.display = 'flex';
                if (headerUserName) headerUserName.textContent = user.nome || 'Gestor';
                if (headerUserRole) {
                    headerUserRole.textContent = 'Gestor';
                    headerUserRole.style.background = 'rgba(0, 184, 148, 0.3)';
                }
            } else {
                if (userWelcome) userWelcome.style.display = 'none';
            }
            // Mostrar botões de perfil e logout
            if (btnPerfil) btnPerfil.style.display = 'flex';
            if (btnLogout) btnLogout.style.display = 'flex';
            if (btnLoginHeader) btnLoginHeader.style.display = 'none';
        } else {
            if (userWelcome) userWelcome.style.display = 'none';
            if (btnPerfil) btnPerfil.style.display = 'none';
            if (btnLogout) btnLogout.style.display = 'none';
            if (btnLoginHeader) btnLoginHeader.style.display = 'flex';
        }
    },
    
    updateMenu() {
        const isLogged = Session.isLogged();
        const isAdmin = Session.isAdmin();
        
        const menuComum = document.getElementById('menuComum');
        const menuAdmin = document.getElementById('menuAdmin');
        const menuNaoLogado = document.getElementById('menuNaoLogado');
        const menuFooter = document.getElementById('menuFooter');
        
        if (isLogged) {
            if (menuComum) menuComum.style.display = 'block';
            if (menuNaoLogado) menuNaoLogado.style.display = 'none';
            if (menuFooter) menuFooter.style.display = 'block';
            if (menuAdmin) {
                menuAdmin.style.display = isAdmin ? 'block' : 'none';
            }
        } else {
            if (menuComum) menuComum.style.display = 'none';
            if (menuAdmin) menuAdmin.style.display = 'none';
            if (menuNaoLogado) menuNaoLogado.style.display = 'block';
            if (menuFooter) menuFooter.style.display = 'none';
        }
    },
    
    updateIndexCards() {
        const isLogged = Session.isLogged();
        const isAdmin = Session.isAdmin();
        
        const cardsUsuarioComum = document.getElementById('cardsUsuarioComum');
        const cardsAdmin = document.getElementById('cardsAdmin');
        const cardsNaoLogado = document.getElementById('cardsNaoLogado');
        
        if (isLogged) {
            if (isAdmin) {
                if (cardsAdmin) cardsAdmin.style.display = 'flex';
                if (cardsUsuarioComum) cardsUsuarioComum.style.display = 'none';
            } else {
                if (cardsUsuarioComum) cardsUsuarioComum.style.display = 'flex';
                if (cardsAdmin) cardsAdmin.style.display = 'none';
            }
            if (cardsNaoLogado) cardsNaoLogado.style.display = 'none';
        } else {
            if (cardsNaoLogado) cardsNaoLogado.style.display = 'flex';
            if (cardsUsuarioComum) cardsUsuarioComum.style.display = 'none';
            if (cardsAdmin) cardsAdmin.style.display = 'none';
        }
    }
};

// ======== MODAL DE PERFIL ========
function abrirModalPerfil() {
    const modal = document.getElementById('modalPerfil');
    const user = Session.get();
    
    if (modal && user) {
        document.getElementById('perfilNome').textContent = user.nome || 'Usuário';
        document.getElementById('perfilMatricula').textContent = user.matricula || '-';
        document.getElementById('perfilTipo').textContent = user.tipo === 'A' ? 'Gestor/Administrador' : 'Usuário Comum';
        modal.classList.add('show');
    }
}

function fecharModalPerfil() {
    const modal = document.getElementById('modalPerfil');
    if (modal) modal.classList.remove('show');
}

// ======== LOGOUT ========
function fazerLogout() {
    if (confirm('Deseja realmente sair do sistema?')) {
        fetch(`${CONFIG.API}/login/logout`, { method: 'POST' }).catch(() => {});
        Session.clear();
        window.location.href = 'login.html';
    }
}

// ======== LOGIN ========
async function handleLogin(e) {
    e.preventDefault();
    const m = document.getElementById("matricula").value.trim();
    const s = document.getElementById("senha").value;
    
    if (!m || !s) { 
        alert("Preencha todos os campos!"); 
        return; 
    }
    
    try {
        const r = await fetch(`${CONFIG.API}/login/validar/${m}/${s}`);
        if (!r.ok) { 
            alert("Credenciais inválidas!"); 
            return; 
        }
        
        const u = await (await fetch(`${CONFIG.API}/usuarios/${m}`)).json();
        Session.save(u);
        window.location.href = "index.html";
    } catch (err) {
        console.error("Erro no login:", err);
        alert("Erro ao fazer login!");
    }
}

// ======== UTILITÁRIO: Extrair ID do agendamento ========
function getAgendamentoId(ag) {
    return ag.id_AGENDAMENTOS || ag.ID_AGENDAMENTOS || ag.idAgendamentos || 
           ag.Id_AGENDAMENTOS || ag.id_agendamentos || ag.idAgendamento;
}

// ======== AGENDAMENTOS ========
const Agendamento = {
    modal: null,
    form: null,

    init() {
        this.modal = document.getElementById("modalAgendar");
        this.form = document.getElementById("formAgendar");
        
        if (!this.modal || !this.form) return;

        document.getElementById("abrirModalBtn")?.addEventListener("click", () => this.abrir());
        document.getElementById("closeModal")?.addEventListener("click", () => this.fechar());
        this.form.addEventListener("submit", e => this.criar(e));
        
        this.carregarAmbientes();
        this.carregarLista();
    },

    abrir() {
        this.modal.classList.add("show");
        const d = document.getElementById("dataEvento");
        if (!d.value) {
            const hoje = new Date();
            d.value = `${hoje.getFullYear()}-${String(hoje.getMonth() + 1).padStart(2, '0')}-${String(hoje.getDate()).padStart(2, '0')}`;
        }
    },

    fechar() {
        this.modal.classList.remove("show");
        this.form.reset();
    },

    async carregarAmbientes() {
        try {
            const amb = await (await fetch(`${CONFIG.API}/ambientes`)).json();
            const sel = document.getElementById("tipoEvento");
            if (!sel) return;
            
            sel.innerHTML = '<option value="">Selecione...</option>';
            amb.forEach(a => {
                const op = document.createElement("option");
                op.value = a.id_AMBIENTES;
                op.textContent = a.nome_ambiente;
                sel.appendChild(op);
            });
        } catch (e) {
            console.error("Erro ao carregar ambientes:", e);
        }

    },

    async criar(e) {
        e.preventDefault();
        
        const u = Session.get();
        // Pegar o ID do usuário
        const userId = u?.id_USUARIO || u?.id_usuario || u?.idUsuario || u?.id;
        
        if (!u || !userId) { 
            alert("Faça login primeiro!"); 
            window.location.href = "login.html"; 
            return; 
        }
        
        console.log("[Agendamento] Criando para usuário ID:", userId);

        const ambienteIdStr = document.getElementById("tipoEvento").value;
        const data = document.getElementById("dataEvento").value;
        const hIni = document.getElementById("horaInicio").value;
        const hFim = document.getElementById("horaFim").value;

        if (!ambienteIdStr || !data || !hIni || !hFim) { 
            alert("Preencha todos os campos!"); 
            return; 
        }

        if (hIni >= hFim) { 
            alert("Hora fim deve ser maior que hora início!"); 
            return; 
        }

        const formatarDataHora = (d, h) => {
            const [ano, mes, dia] = d.split("-");
            const [hh, mm] = h.split(":");
            return `${dia}/${mes}/${ano} ${hh}:${mm}:00`;
        };

        const agora = new Date();
        const dataHoraAgendamento = `${String(agora.getDate()).padStart(2, '0')}/${String(agora.getMonth() + 1).padStart(2, '0')}/${agora.getFullYear()} ${String(agora.getHours()).padStart(2, '0')}:${String(agora.getMinutes()).padStart(2, '0')}:00`;

        const payload = {
            Data_Hora_Agendamento: dataHoraAgendamento,
            Data_Hora_Inicio: formatarDataHora(data, hIni),
            Data_Hora_Fim: formatarDataHora(data, hFim),
            Status_agendamento: "A",
            USUARIO_ID_USUARIO: parseInt(userId),
            AMBIENTE_ID_AMBIENTES: parseInt(ambienteIdStr)
        };

        try {
            const response = await fetch(`${CONFIG.API}/agendamentos`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            if (response.ok) {
                alert(" Agendamento realizado com sucesso!");
                this.fechar();
                this.carregarLista();
            } else if (response.status === 409) {
                alert(" Conflito: O horário já está reservado.");
            } else { 
                alert(" Erro ao agendar");
            }
        } catch (err) {
            alert(" Erro de conexão");
        }
    },

    async carregarLista() {
        const u = Session.get();
        if (!u) return;
        
        const userId = u.id_USUARIO || u.id_usuario || u.idUsuario || u.id;
        if (!userId) return;

        try {
            const ags = await (await fetch(`${CONFIG.API}/agendamentos/usuario/${userId}/futuros`)).json();
            const amb = await (await fetch(`${CONFIG.API}/ambientes`)).json();
            
            const ambientesMap = {};
            amb.forEach(a => ambientesMap[a.id_AMBIENTES] = a.nome_ambiente);
            
            const lista = ags
                .filter(ag => ag.Status_agendamento === 'A')
                .map(ag => ({ ...ag, nomeAmbiente: ambientesMap[ag.AMBIENTE_ID_AMBIENTES] || 'Desconhecido' }));
            
            this.renderLista(lista);
        } catch (e) {
            console.error("Erro:", e);
        }
    },

    renderLista(lista) {
        const tb = document.getElementById("listaAgendamentos");
        if (!tb) return;
        
        tb.innerHTML = "";
        
        if (lista.length === 0) {
            tb.innerHTML = '<tr><td colspan="5" style="text-align:center; padding: 20px;">Nenhum agendamento ativo.</td></tr>';
            return;
        }
        
        lista.forEach(ag => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${ag.nomeAmbiente}</td>
                <td>${ag.Data_Hora_Inicio.split(" ")[0]}</td>
                <td>${ag.Data_Hora_Inicio.split(" ")[1].substring(0,5)}</td>
                <td>${ag.Data_Hora_Fim.split(" ")[1].substring(0,5)}</td>
                <td><span class="status-ativo">Ativo</span></td>
            `;
            tb.appendChild(tr);
        });
    }
};

// ======== MEUS AGENDAMENTOS ========
const MeusAg = {
    async load() {
        const u = Session.get();
        if (!u) { window.location.href = "login.html"; return; }
        
        const userId = u.id_USUARIO || u.id_usuario || u.idUsuario || u.id;
        if (!userId) { window.location.href = "login.html"; return; }

        try {
            const amb = await (await fetch(`${CONFIG.API}/ambientes`)).json();
            let lista = [];
            
            for (const a of amb) {
                const ags = await (await fetch(`${CONFIG.API}/agendamentos/ambiente/${a.id_AMBIENTES}/futuros`)).json();
                ags.forEach(ag => {
                    if (ag.USUARIO_ID_USUARIO === userId) {
                        ag.nomeAmbiente = a.nome_ambiente;
                        lista.push(ag);
                    }
                });
            }
            
            this.render(lista);
        } catch (e) {
            console.error(e);
        }
    },

    render(lista) {
        const tb = document.querySelector("#tabelaMeusAgendamentos tbody");
        if (!tb) return;
        
        tb.innerHTML = "";
        
        if (lista.length === 0) {
            tb.innerHTML = '<tr><td colspan="5" style="text-align:center; padding: 20px;">Nenhum agendamento encontrado.</td></tr>';
            return;
        }
        
        lista.forEach(ag => {
            const status = ag.Status_agendamento === 'A' ? 'Ativo' : 'Cancelado';
            const statusClass = ag.Status_agendamento === 'A' ? 'status-ativo' : 'status-cancelado';
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${ag.nomeAmbiente}</td>
                <td>${ag.Data_Hora_Inicio.split(" ")[0]}</td>
                <td>${ag.Data_Hora_Inicio.split(" ")[1].substring(0,5)}</td>
                <td>${ag.Data_Hora_Fim.split(" ")[1].substring(0,5)}</td>
                <td><span class="${statusClass}">${status}</span></td>
            `;
            tb.appendChild(tr);
        });
    }
};

// ======== CANCELAR AGENDAMENTO (USUÁRIO) ========
const Cancelar = {
    async load() {
        console.log("[Cancelar] Carregando...");
        const u = Session.get();
        console.log("[Cancelar] Usuário da sessão:", u);
        
        if (!u) { 
            console.log("[Cancelar] Usuário não logado, redirecionando...");
            window.location.href = "login.html"; 
            return; 
        }
        
        // Pegar o ID do usuário (pode vir com diferentes nomes)
        const userId = u.id_USUARIO || u.id_usuario || u.idUsuario || u.id;
        console.log("[Cancelar] ID do usuário:", userId);
        
        if (!userId) {
            console.error("[Cancelar] ID do usuário não encontrado na sessão!");
            alert("Erro: Sessão inválida. Faça login novamente.");
            Session.clear();
            window.location.href = "login.html";
            return;
        }

        try {
            const url = `${CONFIG.API}/agendamentos/usuario/${userId}/futuros`;
            console.log("[Cancelar] Buscando agendamentos:", url);
            
            const response = await fetch(url);
            console.log("[Cancelar] Response status:", response.status);
            
            if (!response.ok) {
                console.error("[Cancelar] Erro na resposta:", response.status);
                return;
            }
            
            const ags = await response.json();
            console.log("[Cancelar] Agendamentos recebidos:", ags);
            
            const amb = await (await fetch(`${CONFIG.API}/ambientes`)).json();
            
            const ambientesMap = {};
            amb.forEach(a => ambientesMap[a.id_AMBIENTES] = a.nome_ambiente);
            
            const lista = ags
                .filter(ag => ag.Status_agendamento === 'A')
                .map(ag => ({ ...ag, nomeAmbiente: ambientesMap[ag.AMBIENTE_ID_AMBIENTES] || 'Desconhecido' }));
            
            console.log("[Cancelar] Lista filtrada:", lista);
            this.render(lista);
        } catch (e) {
            console.error("[Cancelar] Erro:", e);
        }
    },

    render(lista) {
        const tb = document.querySelector("#tabelaCancelar tbody");
        if (!tb) return;
        
        tb.innerHTML = "";
        
        if (lista.length === 0) {
            tb.innerHTML = '<tr><td colspan="6" style="text-align:center; padding: 20px;">Nenhum agendamento ativo.</td></tr>';
            return;
        }
        
        lista.forEach(ag => {
            const agId = getAgendamentoId(ag);
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${ag.nomeAmbiente}</td>
                <td>${ag.Data_Hora_Inicio.split(" ")[0]}</td>
                <td>${ag.Data_Hora_Inicio.split(" ")[1].substring(0,5)}</td>
                <td>${ag.Data_Hora_Fim.split(" ")[1].substring(0,5)}</td>
                <td><span class="status-ativo">Ativo</span></td>
                <td><button class="btn-cancelar" data-action="cancelar-ag" data-id="${agId}">Cancelar</button></td>
            `;
            tb.appendChild(tr);
        });
    }
};

// ======== ADMIN - AMBIENTES ========
const AdminAmb = {
    async init() {
        console.log("[AdminAmb] Inicializando...");
        await this.load();
        
        const btnAdd = document.getElementById("btnAdicionarAmbiente");
        if (btnAdd) {
            btnAdd.addEventListener("click", () => this.add());
        }
    },
    
    async load() {
        console.log("[AdminAmb] Carregando...");
        try {
            const response = await fetch(`${CONFIG.API}/ambientes`);
            const amb = await response.json();
            console.log("[AdminAmb] Ambientes:", amb.length);
            
            const tb = document.getElementById("listaAmbientes");
            if (!tb) return;
            
            tb.innerHTML = "";
            
            if (amb.length === 0) {
                tb.innerHTML = '<tr><td colspan="3" style="text-align:center; padding: 20px;">Nenhum ambiente cadastrado.</td></tr>';
                return;
            }
            
            amb.forEach(a => {
                const tr = document.createElement("tr");
                tr.innerHTML = `
                    <td>${a.id_AMBIENTES}</td>
                    <td>${a.nome_ambiente}</td>
                    <td><button class="btnExcluir" data-action="excluir-ambiente" data-id="${a.id_AMBIENTES}">Excluir</button></td>
                `;
                tb.appendChild(tr);
            });
            console.log("[AdminAmb] Tabela renderizada!");
        } catch (e) {
            console.error("[AdminAmb] Erro:", e);
        }
    },
    
    async add() {
        const nome = document.getElementById("novoAmbiente")?.value.trim();
        if (!nome) { 
            alert("Digite o nome do ambiente!"); 
            return; 
        }
        
        try {
            const r = await fetch(`${CONFIG.API}/ambientes`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ nome_ambiente: nome, descricao: nome })
            });
            
            if (r.ok) {
                alert(" Ambiente adicionado!");
                document.getElementById("novoAmbiente").value = "";
                await this.load();
            } else {
                alert(" Erro ao adicionar!");
            }
        } catch (e) {
            console.error(e);
            alert(" Erro de conexão!");
        }
    }
};

// ======== ADMIN - TODOS OS AGENDAMENTOS ========
const TodosAg = {
    async load() {
        console.log("[TodosAg] Carregando...");
        try {
            const amb = await (await fetch(`${CONFIG.API}/ambientes`)).json();
            const usr = await (await fetch(`${CONFIG.API}/usuarios`)).json();
            
            let todos = [];
            for (const a of amb) {
                const ags = await (await fetch(`${CONFIG.API}/agendamentos/ambiente/${a.id_AMBIENTES}/futuros`)).json();
                ags.forEach(x => {
                    x.nomeAmb = a.nome_ambiente;
                    const u = usr.find(us => us.id_USUARIO === x.USUARIO_ID_USUARIO);
                    x.nomeUsr = u ? u.nome : 'N/A';
                    x.matUsr = u ? u.matricula : 'N/A';
                    todos.push(x);
                });
            }
            
            console.log("[TodosAg] Total:", todos.length);
            this.render(todos);
        } catch (e) {
            console.error("[TodosAg] Erro:", e);
        }
    },
    
    render(ags) {
        const tb = document.querySelector("#tabelaAgendamentos tbody");
        if (!tb) return;
        
        tb.innerHTML = "";
        
        if (ags.length === 0) {
            tb.innerHTML = '<tr><td colspan="8" style="text-align:center; padding: 20px;">Nenhum agendamento encontrado.</td></tr>';
            return;
        }
        
        ags.forEach(a => {
            const agId = getAgendamentoId(a);
            const status = a.Status_agendamento === 'A' ? 'Ativo' : 'Cancelado';
            const statusClass = a.Status_agendamento === 'A' ? 'status-ativo' : 'status-cancelado';
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${a.nomeAmb}</td>
                <td>${a.matUsr}</td>
                <td>${a.nomeUsr}</td>
                <td>${a.Data_Hora_Inicio.split(" ")[0]}</td>
                <td>${a.Data_Hora_Inicio.split(" ")[1].substring(0,5)}</td>
                <td>${a.Data_Hora_Fim.split(" ")[1].substring(0,5)}</td>
                <td><span class="${statusClass}">${status}</span></td>
                <td>
                    ${a.Status_agendamento === 'A' 
                        ? `<button class="btn-cancelar" data-action="cancelar-ag-admin" data-id="${agId}">Cancelar</button>
                           <button class="btn-excluir" data-action="excluir-ag-admin" data-id="${agId}" style="margin-left:5px;background:#e74c3c;">Excluir</button>`
                        : `<button class="btn-excluir" data-action="excluir-ag-admin" data-id="${agId}" style="background:#e74c3c;">Excluir</button>`}
                </td>
            `;
            tb.appendChild(tr);
        });
    }
};

// ======== ADMIN - USUÁRIOS ========
const AdminUsr = {
    async init() {
        console.log("[AdminUsr] Inicializando...");
        await this.load();
        
        const btnAdd = document.getElementById("btnAdicionarUsuario");
        if (btnAdd) {
            btnAdd.addEventListener("click", () => this.add());
        }
        
        const filtro = document.getElementById("filtroTipo");
        if (filtro) {
            filtro.addEventListener("change", () => this.load());
        }
    },
    
    async load() {
        console.log("[AdminUsr] Carregando...");
        try {
            const ft = document.getElementById("filtroTipo")?.value;
            const usr = await (await fetch(`${CONFIG.API}/usuarios`)).json();
            
            let usuariosFiltrados = usr;
            if (ft && ft !== 'todos') {
                usuariosFiltrados = usr.filter(u => u.tipo === ft);
            }
            
            console.log("[AdminUsr] Usuários:", usuariosFiltrados.length);
            
            const tb = document.getElementById("listaUsuarios");
            if (!tb) return;
            
            tb.innerHTML = "";
            
            if (usuariosFiltrados.length === 0) {
                tb.innerHTML = '<tr><td colspan="5" style="text-align:center; padding: 20px;">Nenhum usuário encontrado.</td></tr>';
                return;
            }
            
            usuariosFiltrados.forEach(u => {
                const tr = document.createElement("tr");
                tr.innerHTML = `
                    <td>${u.matricula}</td>
                    <td>${u.nome}</td>
                    <td>${u.tipo === 'A' ? 'Administrador' : 'Comum'}</td>
                    <td>${u.senha}</td>
                    <td><button class="btnExcluir" data-action="excluir-usuario" data-id="${u.matricula}">Excluir</button></td>
                `;
                tb.appendChild(tr);
            });
        } catch (e) {
            console.error("[AdminUsr] Erro:", e);
        }
    },
    
    async add() {
        const n = document.getElementById("novoNome")?.value.trim();
        const m = document.getElementById("novaMatricula")?.value.trim();
        const s = document.getElementById("novaSenha")?.value.trim();
        const t = document.getElementById("novoTipo")?.value;
        
        if (!n || !m || !s || !t) { 
            alert("Preencha todos os campos!"); 
            return; 
        }
        
        try {
            const r = await fetch(`${CONFIG.API}/usuarios`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ nome: n, matricula: parseInt(m), senha: s, tipo: t })
            });
            
            if (r.ok) {
                alert(" Usuário adicionado!");
                document.getElementById("novoNome").value = "";
                document.getElementById("novaMatricula").value = "";
                document.getElementById("novaSenha").value = "";
                document.getElementById("novoTipo").value = "C";
                await this.load();
            } else if (r.status === 409) {
                alert(" Matrícula já cadastrada!");
            } else {
                alert(" Erro ao adicionar!");
            }
        } catch (e) {
            console.error(e);
            alert(" Erro de conexão!");
        }
    }
};

// ========================
// FUNÇÕES GLOBAIS DE AÇÃO
// ========================

async function handleExcluirAmbiente(id) {
    console.log("[Action] Excluir ambiente ID:", id);
    
    if (!confirm("Deseja realmente excluir este ambiente?")) return;
    
    try {
        let r = await fetch(`${CONFIG.API}/ambientes/${id}/excluir`, { method: 'POST' });
        if (!r.ok) {
            r = await fetch(`${CONFIG.API}/ambientes/${id}`, { method: 'DELETE' });
        }
        
        if (r.ok) { 
            alert(" Ambiente excluído!"); 
            AdminAmb.load(); 
        } else { 
            alert(" Erro ao excluir!"); 
        }
    } catch (e) {
        console.error(e);
        alert(" Erro de conexão!");
    }
}

async function handleExcluirUsuario(matricula) {
    console.log("[Action] Excluir usuário matrícula:", matricula);
    
    if (!confirm("Deseja realmente excluir este usuário?")) return;
    
    try {
        let r = await fetch(`${CONFIG.API}/usuarios/${matricula}/excluir`, { method: 'POST' });
        if (!r.ok) {
            r = await fetch(`${CONFIG.API}/usuarios/${matricula}`, { method: 'DELETE' });
        }
        
        if (r.ok) {
            alert(" Usuário excluído!"); 
            AdminUsr.load(); 
        } else {
            alert(" Erro ao excluir!"); 
        }
    } catch (e) {
        console.error(e);
        alert(" Erro de conexão!");
    }
}

async function handleCancelarAgendamento(id) {
    console.log("[Action] Cancelar agendamento ID:", id);
    
    if (!confirm("Deseja realmente cancelar este agendamento?")) return;
    
    try {
        let r = await fetch(`${CONFIG.API}/agendamentos/${id}/cancelar`, { method: 'POST' });
        if (!r.ok) {
            r = await fetch(`${CONFIG.API}/agendamentos/${id}/cancelar`, { method: 'PUT' });
        }
        
        if (r.ok) { 
            alert(" Agendamento cancelado!"); 
            // Recarregar a página apropriada
            if (typeof Cancelar !== 'undefined' && document.querySelector("#tabelaCancelar")) {
                Cancelar.load();
            }
            if (typeof TodosAg !== 'undefined' && document.querySelector("#tabelaAgendamentos")) {
                TodosAg.load();
            }
        } else { 
            alert(" Erro ao cancelar!"); 
        }
    } catch (e) {
        console.error(e);
        alert(" Erro de conexão!");
    }
}

async function handleExcluirAgendamento(id) {
    console.log("[Action] Excluir agendamento ID:", id);
    
    if (!confirm("Deseja realmente EXCLUIR PERMANENTEMENTE este agendamento?")) return;
    
    try {
        let r = await fetch(`${CONFIG.API}/agendamentos/${id}/excluir`, { method: 'POST' });
        if (!r.ok) {
            r = await fetch(`${CONFIG.API}/agendamentos/${id}`, { method: 'DELETE' });
        }
        
        if (r.ok) { 
            alert("Agendamento excluído!"); 
            TodosAg.load(); 
        } else { 
            alert(" Erro ao excluir!"); 
        }
    } catch (e) {
        console.error(e);
        alert(" Erro de conexão!");
    }
}

// =====================================
// Captura cliques em botões dinâmicos
// =====================================

document.addEventListener("click", function(e) {
    const target = e.target;
    
    // Verifica se é um botão com data-action
    if (target.matches("[data-action]")) {
        const action = target.getAttribute("data-action");
        const id = target.getAttribute("data-id");
        
        console.log("[Click] Action:", action, "ID:", id);
        
        switch (action) {
            case "excluir-ambiente":
                handleExcluirAmbiente(id);
                break;
            case "excluir-usuario":
                handleExcluirUsuario(id);
                break;
            case "cancelar-ag":
            case "cancelar-ag-admin":
                handleCancelarAgendamento(id);
                break;
            case "excluir-ag-admin":
                handleExcluirAgendamento(id);
                break;
        }
    }
});

// ======== INICIALIZAÇÃO ========
document.addEventListener("DOMContentLoaded", () => {
    console.log("[Init] Página carregada");
    
    UIController.init();
    
    const f = document.getElementById("form-login");
    if (f) f.addEventListener("submit", handleLogin);
    
    const p = window.location.pathname;
    console.log("[Init] Path:", p);
    
    if (p.includes("agendar.html")) {
        if (!Session.isLogged()) { window.location.href = "login.html"; return; }
        Agendamento.init();
    } else if (p.includes("meus_agendamentos.html")) {
        if (!Session.isLogged()) { window.location.href = "login.html"; return; }
        MeusAg.load();
    } else if (p.includes("cancelar.html")) {
        if (!Session.isLogged()) { window.location.href = "login.html"; return; }
        Cancelar.load();
    } else if (p.includes("editar_ambiente.html")) {
        if (!Session.isAdmin()) { window.location.href = "index.html"; return; }
        AdminAmb.init();
    } else if (p.includes("todos_agendamentos.html")) {
        if (!Session.isAdmin()) { window.location.href = "index.html"; return; }
        TodosAg.load();
    } else if (p.includes("gerenciar_usuario.html")) {
        if (!Session.isAdmin()) { window.location.href = "index.html"; return; }
        AdminUsr.init();
    }
    
    const modalPerfil = document.getElementById('modalPerfil');
    if (modalPerfil) {
        modalPerfil.addEventListener('click', (e) => {
            if (e.target === modalPerfil) fecharModalPerfil();
        });
    }
});
