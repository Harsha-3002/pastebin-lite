import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import './ViewPaste.css';

const API_URL = import.meta.env.VITE_API_URL;

function ViewPaste() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [paste, setPaste] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchPaste();
  }, [id]);

  const fetchPaste = async () => {
    try {
      const response = await axios.get(`${API_URL}/pastes/${id}`);
      setPaste(response.data);
      setLoading(false);
    } catch (err) {
      setError('Paste not found or has expired');
      setLoading(false);
    }
  };

  const copyContent = () => {
    navigator.clipboard.writeText(paste.content);
    alert('Content copied to clipboard!');
  };

  if (loading) {
    return (
      <div className="view-paste-container">
        <div className="view-paste-card">
          <div className="loading">Loading paste...</div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="view-paste-container">
        <div className="view-paste-card error-card">
          <h1>❌ {error}</h1>
          <p>This paste doesn't exist or has expired.</p>
          <button onClick={() => navigate('/')} className="home-btn">
            Create New Paste
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="view-paste-container">
      <div className="view-paste-card">
        <div className="header">
          <h1>📋 Pastebin Lite</h1>
          <button onClick={() => navigate('/')} className="new-paste-btn">
            + New Paste
          </button>
        </div>

        <div className="paste-info">
          <span className="paste-id">ID: {id}</span>
          {paste.remaining_views !== null && (
            <span className="views-info">
              👁️ {paste.remaining_views} views remaining
            </span>
          )}
          {paste.expires_at && (
            <span className="expires-info">
              ⏰ Expires: {new Date(paste.expires_at).toLocaleString()}
            </span>
          )}
        </div>

        <div className="content-box">
          <div className="content-header">
            <span>Content</span>
            <button onClick={copyContent} className="copy-content-btn">
              📋 Copy
            </button>
          </div>
          <pre className="paste-content">{paste.content}</pre>
        </div>
      </div>
    </div>
  );
}

export default ViewPaste;