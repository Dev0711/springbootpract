import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useToast } from '../components/Toast';

export default function Login() {
  const { login } = useAuth();
  const toast = useToast();
  const navigate = useNavigate();

  const [formData, setFormData] = useState({ email: '', password: '' });
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);
  const [showPassword, setShowPassword] = useState(false);

  const validate = () => {
    const newErrors = {};
    if (!formData.email.trim()) {
      newErrors.email = 'Email is required';
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) {
      newErrors.email = 'Invalid email format';
    }
    if (!formData.password) {
      newErrors.password = 'Password is required';
    }
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
    if (errors[name]) setErrors((prev) => ({ ...prev, [name]: '' }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validate()) return;

    setLoading(true);
    try {
      await login(formData);
      toast.success('Login successful! Welcome back.');
      navigate('/');
    } catch (err) {
      toast.error(err.message || 'Login failed. Please check your credentials.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-[calc(100vh-72px)] flex items-center justify-center p-8 px-6">
      <div className="animate-fade-in-up w-full max-w-[460px] glass rounded-3xl p-10 shadow-2xl backdrop-blur-[30px]">
        {/* Header */}
        <div className="text-center mb-8">
          <h1 className="text-2xl font-extrabold mb-2">Welcome Back</h1>
          <p className="text-sm text-text-secondary">Sign in to your EcomAce account</p>
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit} className="flex flex-col gap-5">
          {/* Email */}
          <div className="flex flex-col gap-2">
            <label htmlFor="login-email" className="text-sm font-medium text-text-secondary">
              Email Address
            </label>
            <input
              id="login-email"
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              placeholder="you@example.com"
              className={`px-4 py-3 bg-bg-input border rounded-lg text-text-primary text-base outline-none
                transition-all duration-200 placeholder:text-text-muted
                focus:bg-bg-input-focus focus:border-border-focus focus:ring-2 focus:ring-accent/15
                ${errors.email ? 'border-error ring-2 ring-error/10' : 'border-border-subtle'}`}
            />
            {errors.email && (
              <span className="text-xs text-error flex items-center gap-1">⚠ {errors.email}</span>
            )}
          </div>

          {/* Password */}
          <div className="flex flex-col gap-2">
            <label htmlFor="login-password" className="text-sm font-medium text-text-secondary">
              Password
            </label>
            <div className="relative">
              <input
                id="login-password"
                type={showPassword ? 'text' : 'password'}
                name="password"
                value={formData.password}
                onChange={handleChange}
                placeholder="••••••••"
                className={`w-full px-4 py-3 pr-12 bg-bg-input border rounded-lg text-text-primary text-base outline-none
                  transition-all duration-200 placeholder:text-text-muted
                  focus:bg-bg-input-focus focus:border-border-focus focus:ring-2 focus:ring-accent/15
                  ${errors.password ? 'border-error ring-2 ring-error/10' : 'border-border-subtle'}`}
              />
              <button
                type="button"
                onClick={() => setShowPassword(!showPassword)}
                className="absolute right-3 top-1/2 -translate-y-1/2 bg-transparent border-none text-text-muted text-sm cursor-pointer hover:text-text-primary transition-colors"
              >
                {showPassword ? '🙈' : '👁️'}
              </button>
            </div>
            {errors.password && (
              <span className="text-xs text-error flex items-center gap-1">⚠ {errors.password}</span>
            )}
          </div>

          {/* Forgot Password */}
          <div className="text-right">
            <Link to="/forgot-password" className="text-sm text-accent font-medium hover:text-accent-hover transition-colors no-underline">
              Forgot Password?
            </Link>
          </div>

          {/* Submit */}
          <button
            type="submit"
            disabled={loading}
            className="w-full py-3.5 btn-gradient text-white font-semibold rounded-xl text-base
              transition-all duration-200 cursor-pointer disabled:opacity-50 disabled:cursor-not-allowed
              flex items-center justify-center gap-2 border-none"
          >
            {loading ? (
              <>
                <span className="spinner w-5 h-5" />
                Signing in...
              </>
            ) : (
              'Sign In'
            )}
          </button>
        </form>

        {/* Footer */}
        <p className="text-center mt-6 text-sm text-text-secondary">
          Don't have an account?{' '}
          <Link to="/register" className="font-semibold text-accent hover:text-accent-hover no-underline">
            Create one
          </Link>
        </p>
      </div>
    </div>
  );
}
