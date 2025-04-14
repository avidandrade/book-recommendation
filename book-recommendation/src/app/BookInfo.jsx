import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Button } from '@/components/ui/button';

const BookInfo = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [book, setBook] = useState(null);
  const [loading, setLoading] = useState(true);
  const [reviewSummary, setReviewSummary] = useState([]);
  const [summary, setSummary] = useState([]);

  useEffect(() => {
    const fetchBookInfo = async () => {
      try {
        const token = localStorage.getItem('token');
        const response = await fetch(`http://localhost:8080/books/${id}`,{
          headers:{
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json",
          }
        });
        const data = await response.json();
        setBook(data);

        if(data.title && data.rating){
          const [reviewResponse, summary] = await Promise.all([
            fetch(`http://localhost:8080/review?title=${data.title}&rating=${data.rating}`),
            fetch(`http://localhost:8080/summary?title=${data.title}`)
          ]);

          const reviewText = await reviewResponse.text();
          const summaryText = await summary.text();

          setReviewSummary(reviewText);
          setSummary(summaryText);
        }
      } catch (error) {
        console.error('Error fetching book information', error);
      } finally {
        setLoading(false);
      }
    };

    fetchBookInfo();
  }, [id]);

  const goBack = () => {
    navigate('/books');
  };

  if (loading) {
    return <div>Loading...</div>;
  }

  if (!book) {
    return <div>Book not found</div>;
  }
  return (
    <div className="p-6 max-w-4xl mx-auto shadow-md fade-in">
      <Button onClick={goBack} className="mb-4">Back</Button>
      <div className="flex flex-row items-center gap-7">
        <div className="w-1/3">
          <h1 className="text-2xl font-bold">{book.title}</h1>
          <p className="text-lg mt-2">By {Array.isArray(book.authors) ? book.authors.join(', ') : 'Unknown'}</p>
          <img
          src={book.coverImageUrl}
          alt="Book Cover"
          className="h-96 w-64 object-cover mt-4"
          />
        </div>
      <div className="w-2/3">
        <h2 className="text-xl font-bold p-2">Description</h2>
        <p className="text-sm text-white-600 line-clamp-6">{book.description}</p>
        <div className="mt-4">
          <h2 className="text-xl font-bold">Review</h2> 
          {reviewSummary ? (
            <p className="text-sm">{reviewSummary}</p>
          ) : (
            <p>No reviews available.</p>
          )}
          <h2 className="text-xl font-bold p-2">Summary</h2>
          {summary ? (
            <p className="text-sm">{summary}</p>
          ) : (
            <p>No summary available.</p>
          )}
        </div>
      </div>
      </div>
    </div>
  );
};

export default BookInfo;