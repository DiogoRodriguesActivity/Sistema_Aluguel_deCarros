import { useState } from 'react'
import { api } from '../api.js'

const vazio = {
  nome: '', cpf: '', rg: '', endereco: '',
  login: '', profissao: '', senha: '', confirmar: '',
}

export default function Cadastro() {
  const [form, setForm] = useState(vazio)
  const [rendimentos, setRendimentos] = useState([])
  const [erro, setErro] = useState('')
  const [ok, setOk] = useState(false)
  const [enviando, setEnviando] = useState(false)

  const mudar = (e) => setForm({ ...form, [e.target.name]: e.target.value })

  const mudarRendimento = (i, campo, valor) =>
    setRendimentos(rendimentos.map((r, idx) => (idx === i ? { ...r, [campo]: valor } : r)))

  async function enviar(e) {
    e.preventDefault()
    setErro('')
    setOk(false)
    if (form.senha !== form.confirmar) return setErro('As senhas não conferem')

    setEnviando(true)
    try {
      const { confirmar, ...dados } = form
      await api('/api/clientes', {
        method: 'POST',
        body: JSON.stringify({
          ...dados,
          rendimentos: rendimentos.map((r) => ({
            entidadeEmpregadora: r.entidadeEmpregadora,
            valor: Number(r.valor),
          })),
        }),
      })
      setOk(true)
      setForm(vazio)
      setRendimentos([])
    } catch (err) {
      setErro(err.message)
    } finally {
      setEnviando(false)
    }
  }

  return (
    <main className="card">
      <h1>Vamos abrir novos caminhos?</h1>
      <p className="sub">Crie sua conta e encontre o carro para chamar de seu.</p>

      <form onSubmit={enviar}>
        <label>Nome
          <input name="nome" value={form.nome} onChange={mudar} required />
        </label>
        <div className="linha">
          <label>CPF
            <input name="cpf" value={form.cpf} onChange={mudar} placeholder="000.000.000-00" required />
          </label>
          <label>RG
            <input name="rg" value={form.rg} onChange={mudar} required />
          </label>
        </div>
        <label>Endereço
          <input name="endereco" value={form.endereco} onChange={mudar} required />
        </label>
        <label>E-mail
          <input type="email" name="login" value={form.login} onChange={mudar} required />
        </label>
        <label>Profissão
          <input name="profissao" value={form.profissao} onChange={mudar} />
        </label>

        <fieldset>
          <legend>Empregadores e rendimentos (até 3)</legend>
          {rendimentos.map((r, i) => (
            <div className="linha" key={i}>
              <input
                placeholder="Entidade empregadora"
                value={r.entidadeEmpregadora}
                onChange={(e) => mudarRendimento(i, 'entidadeEmpregadora', e.target.value)}
                required
              />
              <input
                type="number" min="0" step="0.01" placeholder="Rendimento (R$)"
                value={r.valor}
                onChange={(e) => mudarRendimento(i, 'valor', e.target.value)}
                required
              />
              <button type="button" className="sec"
                onClick={() => setRendimentos(rendimentos.filter((_, idx) => idx !== i))}>
                Remover
              </button>
            </div>
          ))}
          <button type="button" className="sec" disabled={rendimentos.length >= 3}
            onClick={() => setRendimentos([...rendimentos, { entidadeEmpregadora: '', valor: '' }])}>
            + Adicionar rendimento
          </button>
        </fieldset>

        <label>Crie uma senha
          <input type="password" name="senha" value={form.senha} onChange={mudar} required />
          <small>Mínimo de 8 caracteres, com uma letra e um número.</small>
        </label>
        <label>Confirme sua senha
          <input type="password" name="confirmar" value={form.confirmar} onChange={mudar} required />
        </label>

        {erro && <p className="erro">{erro}</p>}
        {ok && <p className="sucesso">Conta criada com sucesso!</p>}

        <button type="submit" disabled={enviando}>
          {enviando ? 'Criando...' : 'Criar minha conta'}
        </button>
      </form>
    </main>
  )
}