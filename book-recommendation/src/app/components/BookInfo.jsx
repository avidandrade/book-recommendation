import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Button } from '@/components/ui/button';

const BookInfo = () => {
  const { isbn } = useParams();
  const navigate = useNavigate();
  const [book, setBook] = useState(null);
  const [isloading, setLoading] = useState(true);
  const [reviewSummary, setReviewSummary] = useState([]);
  const [summary, setSummary] = useState([]);
  const backend_url = import.meta.env.VITE_BACKEND_URL;

  useEffect(() => {
    const fetchBookInfo = async () => {
      try {

        const response = await fetch(`${backend_url}/books/${isbn}`,{
          headers:{
            "Content-Type": "application/json",
          },
          credentials: 'include',
        });

        if(!response.ok){
          throw new error('Failed to fetch book details');
        }
        
        const data = await response.json();
        setBook(data);

        if(data.title && data.rating){
          const [reviewResponse, summary] = await Promise.all([
            fetch(`${backend_url}/review?title=${data.title}&rating=${data.rating}`, {
              credentials: 'include'
            }),
            fetch(`${backend_url}/summary?title=${data.title}`, {
              credentials:'include'
            })
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
  }, [isbn]);

  const goBack = () => {
    navigate('/books');
  };

  if (isloading) {
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
        <p className="text-sm text-white-600 line-clamp-10">{book.description}</p>
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