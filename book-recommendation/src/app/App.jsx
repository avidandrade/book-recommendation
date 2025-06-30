
import BookCards from './components/BookCards.jsx'
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import BookInfo from './components/BookInfo.jsx';
import SignUp from './routes/sign-up.jsx';
import Login from './routes/login.jsx';
import ForgotPassword from './routes/forgot-password.jsx';
import './App.css'
import { AuthProvider, useAuth } from './components/AuthContext.jsx'; 

function AppRoutes() {
  const {isAuthenticated, isLoading} = useAuth();

  if (isLoading) {
    return <div>Loading...</div>;
  }

  return (
      <Routes>
        <Route path="/" element={<Navigate to="/login" />} />
        <Route path="/login" element={<Login />} />
        <Route path="/sign-up" element={<SignUp/>}/>
        <Route path="/forgot-password" element={<ForgotPassword/>}/>
        <Route path="/books" element={isAuthenticated ? <BookCards /> : <Navigate to="/login"/>}/>
        <Route path="/books/:isbn" element={isAuthenticated ? <BookInfo/> : <Navigate to="/login"/>} />
      </Routes>
  )
}

export default function App(){
  return (
    <AuthProvider>
      <Router>
        <AppRoutes/>
      </Router>
    </AuthProvider>
  );
}
