const API_URL = import.meta.env.VITE_API_URL ?? 'http://localhost:8080'

export async function api(path, options = {}) {
  const token = localStorage.getItem('token')
  const res = await fetch(`${API_URL}${path}`, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...(token && { Authorization: `Bearer ${token}` }),
      ...options.headers,
    },
  })

  if (!res.ok) {
    if (res.status === 409) throw new Error('E-mail ou CPF já cadastrado')
    if (res.status === 400)
      throw new Error('Dados inválidos. Confira o CPF e a senha (mínimo 8 caracteres, com letra e número).')
    throw new Error(`Erro ${res.status}`)
  }
  return res.status === 204 ? null : res.json()
}