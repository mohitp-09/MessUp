import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './page/home';
import Auth from './components/auth/Auth'; 

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/auth" element={<Auth />} />
      </Routes>
    </Router>
  );
}

export default App;
