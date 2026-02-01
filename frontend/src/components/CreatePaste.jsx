import { useState } from 'react';
import axios from 'axios';
import './CreatePaste.css';

const API_URL = import.meta.env.VITE_API_URL;

function CreatePaste() {
  const [content, setContent] = useState('');
  const [ttlSeconds, setTtlSeconds] = useState('');
  const [maxViews, setMaxViews] = useState('');
  const [pasteUrl, setPasteUrl] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setPasteUrl('');
    setLoading(true);

    try {
      const payload = {
        content: content,
      };

      if (ttlSeconds && parseInt(ttlSeconds) > 0) {
        payload.ttl_seconds = parseInt(ttlSeconds);
      }

      if (maxViews && parseInt(maxViews) > 0) {
        payload.max_views = parseInt(maxViews);
      }

      const response = await axios.post(`${API_URL}/pastes`, payload);
      
      const frontendUrl = `${window.location.origin}/p/${response.data.id}`;
      setPasteUrl(frontendUrl);
      
      setContent('');
      setTtlSeconds('');
      setMaxViews('');
    } catch (err) {
      if (err.response && err.response.data) {
        setError(err.response.data.message || 'Failed to create paste');
      } else {
        setError('Failed to create paste. Please try again.');
      }
    } finally {
      setLoading(false);
    }
  };

  const copyToClipboard = () => {
    navigator.clipboard.writeText(pasteUrl);
    alert('URL copied to clipboard!');
  };

  return (
    <div className="create-paste-container">
      <div className="create-paste-card">
        <h1>📋 Pastebin Lite</h1>
        <p className="subtitle">Share text snippets instantly</p>

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="content">Paste Content *</label>
            <textarea
              id="content"
              value={content}
              onChange={(e) => setContent(e.target.value)}
              placeholder="Enter your text here..."
              rows="12"
              required
            />
          </div>

          <div className="options-row">
            <div className="form-group">
              <label htmlFor="ttl">Expire After (seconds)</label>
              <input
                type="number"
                id="ttl"
                value={ttlSeconds}
                onChange={(e) => setTtlSeconds(e.target.value)}
                placeholder="Optional"
                min="1"
              />
            </div>

            <div className="form-group">
              <label htmlFor="maxViews">Max Views</label>
              <input
                type="number"
                id="maxViews"
                value={maxViews}
                onChange={(e) => setMaxViews(e.target.value)}
                placeholder="Optional"
                min="1"
              />
            </div>
          </div>

          {error && <div className="error-message">{error}</div>}

          <button type="submit" className="submit-btn" disabled={loading}>
            {loading ? 'Creating...' : 'Create Paste'}
          </button>
        </form>

        {pasteUrl && (
          <div className="success-box">
            <h3>✅ Paste Created Successfully!</h3>
            <div className="url-box">
              <input type="text" value={pasteUrl} readOnly />
              <button onClick={copyToClipboard} className="copy-btn">
                Copy
              </button>
            </div>
            <a href={pasteUrl} target="_blank" rel="noopener noreferrer" className="view-link">
              View Paste →
            </a>
          </div>
        )}
      </div>
    </div>
  );
}

export default CreatePaste;