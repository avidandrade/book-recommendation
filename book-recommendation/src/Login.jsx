import React, { useEffect } from 'react';
import { useAuth0 } from '@auth0/auth0-react';
import { useNavigate } from 'react-router-dom';

const Login = () => {
  const { loginWithRedirect, isAuthenticated } = useAuth0();
  const navigate = useNavigate();

  useEffect(() => {
    if (isAuthenticated) {
      navigate('/book-cards');
    }
  }, [isAuthenticated, navigate]);

  return (
    <div>
      <h2>Please log in to view book recommendations.</h2>
      <button onClick={() => loginWithRedirect()}>Log In</button>
    </div>
  );
};

export default Login;