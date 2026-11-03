import { Routes, Route, Navigate } from 'react-router-dom'
import Cadastro from './pages/Cadastro.jsx'

export default function App() {
  return (
    <Routes>
      <Route path="/cadastro" element={<Cadastro />} />
      <Route path="*" element={<Navigate to="/cadastro" />} />
    </Routes>
  )
}