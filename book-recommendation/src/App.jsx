
import BookCards from './BookCards.jsx'
import './App.css'

function App() {

  return (

    <div className='p- max-w-4xl mx-auto'>
        <h1>Book Recommendation</h1>
        <p className='p-3'>Discover book recommendations based on your feelings!</p>
        <BookCards />
    </div>
  )
}

export default App
