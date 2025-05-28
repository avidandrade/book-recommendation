import { createContext, useContext, useEffect, useState} from "react";
import { supabase } from "../routes/supabaseClient";

const AuthContext = createContext();

export const AuthProvider = ({children}) => {
    const [session, setSession] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    
    useEffect(() =>{
        supabase.auth.getSession().then(({ data }) =>{
            setSession(data.session);
            setIsLoading(false);
        });

        const {data: listener } = supabase.auth.onAuthStateChange((_event, session) => {
            setSession(session);
        });

        return () => {
            listener.subscription.unsubscribe();
        };

    },[]);
    
    return (
    <AuthContext.Provider value={{ session, isAuthenticated: !!session, isLoading}}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);