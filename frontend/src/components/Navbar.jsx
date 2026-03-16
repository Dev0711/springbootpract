import { useState, useEffect } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Navbar() {
  const { isAuthenticated, user, logout } = useAuth();
  const location = useLocation();
  const [scrolled, setScrolled] = useState(false);
  const [mobileOpen, setMobileOpen] = useState(false);

  useEffect(() => {
    const handleScroll = () => setScrolled(window.scrollY > 20);
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  useEffect(() => {
    setMobileOpen(false);
  }, [location.pathname]);

  const handleLogout = async () => {
    await logout();
  };

  const getInitials = () => {
    if (!user) return '?';
    const name = user.name || user.firstName || user.email || '';
    const parts = name.trim().split(/\s+/);
    if (parts.length >= 2) return (parts[0][0] + parts[1][0]).toUpperCase();
    return name.slice(0, 2).toUpperCase();
  };

  return (
    <nav
      className={`fixed top-0 left-0 right-0 h-[72px] flex items-center z-50 transition-all duration-300
        backdrop-blur-xl border-b border-border-subtle
        ${scrolled ? 'bg-bg-primary/95 shadow-lg' : 'bg-bg-primary/80'}`}
    >
      <div className="w-full max-w-[1200px] mx-auto px-6 flex items-center justify-between">
        {/* Brand */}
        <Link to="/" className="flex items-center gap-2 no-underline">
          <span className="w-9 h-9 btn-gradient rounded-lg flex items-center justify-center text-lg">
            ⚡
          </span>
          <span className="gradient-text text-xl font-extrabold tracking-tight">EcomAce</span>
        </Link>

        {/* Mobile Toggle */}
        <button
          className="md:hidden bg-transparent border-none text-text-primary text-xl cursor-pointer p-2"
          onClick={() => setMobileOpen(!mobileOpen)}
          aria-label="Toggle navigation"
        >
          {mobileOpen ? '✕' : '☰'}
        </button>

        {/* Nav Links */}
        <ul
          className={`items-center gap-2 list-none
            max-md:absolute max-md:top-[72px] max-md:left-0 max-md:right-0 max-md:flex-col
            max-md:bg-bg-primary/97 max-md:backdrop-blur-xl max-md:border-b max-md:border-border-subtle
            max-md:p-4 max-md:animate-slide-down
            ${mobileOpen ? 'flex' : 'max-md:hidden md:flex'}`}
        >
          <li>
            <Link
              to="/"
              className={`px-4 py-2 text-sm font-medium rounded-md transition-all duration-150 no-underline
                max-md:w-full max-md:text-left max-md:px-4 max-md:py-3
                ${location.pathname === '/' ? 'text-accent' : 'text-text-secondary hover:text-text-primary hover:bg-bg-glass'}`}
            >
              Home
            </Link>
          </li>

          {isAuthenticated ? (
            <>
              <li>
                <Link
                  to="/profile"
                  className={`px-4 py-2 text-sm font-medium rounded-md transition-all duration-150 no-underline
                    max-md:w-full max-md:text-left max-md:px-4 max-md:py-3
                    ${location.pathname === '/profile' ? 'text-accent' : 'text-text-secondary hover:text-text-primary hover:bg-bg-glass'}`}
                >
                  Profile
                </Link>
              </li>
              <li>
                <div className="flex items-center gap-3">
                  <div className="w-9 h-9 rounded-full btn-gradient flex items-center justify-center font-bold text-sm text-white">
                    {getInitials()}
                  </div>
                  <span className="font-semibold text-sm text-text-primary">
                    {user?.name || user?.firstName || user?.email?.split('@')[0] || 'User'}
                  </span>
                </div>
              </li>
              <li>
                <button
                  onClick={handleLogout}
                  className="px-4 py-2 text-sm font-semibold rounded-lg glass glass-hover cursor-pointer
                    text-text-primary transition-all duration-200"
                >
                  Logout
                </button>
              </li>
            </>
          ) : (
            <>
              <li>
                <Link
                  to="/login"
                  className={`px-4 py-2 text-sm font-medium rounded-md transition-all duration-150 no-underline
                    max-md:w-full max-md:text-left max-md:px-4 max-md:py-3
                    ${location.pathname === '/login' ? 'text-accent' : 'text-text-secondary hover:text-text-primary hover:bg-bg-glass'}`}
                >
                  Login
                </Link>
              </li>
              <li>
                <Link
                  to="/register"
                  className="px-4 py-2 text-sm font-semibold rounded-lg btn-gradient text-white no-underline
                    transition-all duration-200"
                >
                  Get Started
                </Link>
              </li>
            </>
          )}
        </ul>
      </div>
    </nav>
  );
}
