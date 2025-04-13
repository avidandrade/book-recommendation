
import BookCards from './BookCards.jsx'
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import BookInfo from './BookInfo.jsx';
import SignUp from './routes/sign-up.jsx';
import Login from './routes/Login.jsx';
import { useState, useEffect } from 'react';
import ForgotPassword from './routes/forgot-password.jsx';
import './App.css'

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(!!localStorage.getItem('token'));

  useEffect(() => {
    const token = localStorage.getItem('token');
    setIsAuthenticated(!!token);
  }, []);

  return (
    <Router>
      <Routes>
        <Route path="/" element={<Navigate to="/login" />} />
        <Route path="/login" element={<Login onLogin={() => setIsAuthenticated(true)} />} />
        <Route path="/sign-up" element={<SignUp/>}/>
        <Route path="/forgot-password" element={<ForgotPassword/>}/>
        <Route path="/books" element={isAuthenticated ? <BookCards /> : <Navigate to="/login"/>}/>
        <Route path="/books/:id" element={isAuthenticated ? <BookInfo/> : <Navigate to="/login"/>} />
      </Routes>
    </Router>
  )
}

export default App
