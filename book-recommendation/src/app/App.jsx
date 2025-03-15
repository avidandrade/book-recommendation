
import BookCards from './BookCards.jsx'
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import BookInfo from './BookInfo.jsx';
import './App.css'

function App() {

  return (
    <Router>
      <Routes>
        <Route path="/" element={<BookCards />} />
        <Route path="/books/:id" element={<BookInfo/>} />
      </Routes>
    </Router>
  )
}

export default App
