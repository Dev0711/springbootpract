import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useToast } from '../components/Toast';

export default function Register() {
  const { register } = useAuth();
  const toast = useToast();
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    phoneNumber: '',
    password: '',
    confirmPassword: '',
  });
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);
  const [showPassword, setShowPassword] = useState(false);

  const validate = () => {
    const e = {};
    if (!formData.firstName.trim()) e.firstName = 'First name is required';
    else if (formData.firstName.trim().length < 2) e.firstName = 'At least 2 characters';
    else if (formData.firstName.trim().length > 50) e.firstName = 'Max 50 characters';

    if (!formData.lastName.trim()) e.lastName = 'Last name is required';
    else if (formData.lastName.trim().length < 2) e.lastName = 'At least 2 characters';
    else if (formData.lastName.trim().length > 50) e.lastName = 'Max 50 characters';

    if (!formData.email.trim()) e.email = 'Email is required';
    else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) e.email = 'Invalid email format';

    if (formData.phoneNumber && !/^[0-9]{10}$/.test(formData.phoneNumber))
      e.phoneNumber = 'Phone number must be 10 digits';

    if (!formData.password) e.password = 'Password is required';
    else if (formData.password.length < 8) e.password = 'At least 8 characters';
    else if (!/(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])/.test(formData.password))
      e.password = 'Must include uppercase, lowercase, digit & special char (@#$%^&+=)';

    if (!formData.confirmPassword) e.confirmPassword = 'Please confirm your password';
    else if (formData.password !== formData.confirmPassword) e.confirmPassword = 'Passwords do not match';

    setErrors(e);
    return Object.keys(e).length === 0;
  };

  const handleChange = (ev) => {
    const { name, value } = ev.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
    if (errors[name]) setErrors((prev) => ({ ...prev, [name]: '' }));
  };

  const handleSubmit = async (ev) => {
    ev.preventDefault();
    if (!validate()) return;

    setLoading(true);
    try {
      const { confirmPassword, ...payload } = formData;
      await register(payload);
      toast.success('Account created successfully! Welcome aboard 🎉');
      navigate('/');
    } catch (err) {
      toast.error(err.message || 'Registration failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const inputClass = (field) =>
    `w-full px-4 py-3 bg-bg-input border rounded-lg text-text-primary text-base outline-none
     transition-all duration-200 placeholder:text-text-muted
     focus:bg-bg-input-focus focus:border-border-focus focus:ring-2 focus:ring-accent/15
     ${errors[field] ? 'border-error ring-2 ring-error/10' : 'border-border-subtle'}`;

  return (
    <div className="min-h-[calc(100vh-72px)] flex items-center justify-center py-12 px-6">
      <div className="animate-fade-in-up w-full max-w-[520px] glass rounded-3xl p-10 shadow-2xl backdrop-blur-[30px]">
        {/* Header */}
        <div className="text-center mb-8">
          <h1 className="text-2xl font-extrabold mb-2">Create Your Account</h1>
          <p className="text-sm text-text-secondary">Join EcomAce and start shopping today</p>
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit} className="flex flex-col gap-5">
          {/* Name Row */}
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div className="flex flex-col gap-2">
              <label htmlFor="reg-firstName" className="text-sm font-medium text-text-secondary">First Name</label>
              <input
                id="reg-firstName"
                name="firstName"
                value={formData.firstName}
                onChange={handleChange}
                placeholder="John"
                className={inputClass('firstName')}
              />
              {errors.firstName && <span className="text-xs text-error">⚠ {errors.firstName}</span>}
            </div>
            <div className="flex flex-col gap-2">
              <label htmlFor="reg-lastName" className="text-sm font-medium text-text-secondary">Last Name</label>
              <input
                id="reg-lastName"
                name="lastName"
                value={formData.lastName}
                onChange={handleChange}
                placeholder="Doe"
                className={inputClass('lastName')}
              />
              {errors.lastName && <span className="text-xs text-error">⚠ {errors.lastName}</span>}
            </div>
          </div>

          {/* Email */}
          <div className="flex flex-col gap-2">
            <label htmlFor="reg-email" className="text-sm font-medium text-text-secondary">Email Address</label>
            <input
              id="reg-email"
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              placeholder="you@example.com"
              className={inputClass('email')}
            />
            {errors.email && <span className="text-xs text-error">⚠ {errors.email}</span>}
          </div>

          {/* Phone */}
          <div className="flex flex-col gap-2">
            <label htmlFor="reg-phone" className="text-sm font-medium text-text-secondary">
              Phone Number <span className="text-text-muted">(optional)</span>
            </label>
            <input
              id="reg-phone"
              type="tel"
              name="phoneNumber"
              value={formData.phoneNumber}
              onChange={handleChange}
              placeholder="9876543210"
              maxLength={10}
              className={inputClass('phoneNumber')}
            />
            {errors.phoneNumber && <span className="text-xs text-error">⚠ {errors.phoneNumber}</span>}
          </div>

          {/* Password */}
          <div className="flex flex-col gap-2">
            <label htmlFor="reg-password" className="text-sm font-medium text-text-secondary">Password</label>
            <div className="relative">
              <input
                id="reg-password"
                type={showPassword ? 'text' : 'password'}
                name="password"
                value={formData.password}
                onChange={handleChange}
                placeholder="Min 8 chars, mix of upper/lower/digit/special"
                className={`${inputClass('password')} pr-12`}
              />
              <button
                type="button"
                onClick={() => setShowPassword(!showPassword)}
                className="absolute right-3 top-1/2 -translate-y-1/2 bg-transparent border-none text-text-muted text-sm cursor-pointer hover:text-text-primary transition-colors"
              >
                {showPassword ? '🙈' : '👁️'}
              </button>
            </div>
            {errors.password && <span className="text-xs text-error">⚠ {errors.password}</span>}
          </div>

          {/* Confirm Password */}
          <div className="flex flex-col gap-2">
            <label htmlFor="reg-confirm" className="text-sm font-medium text-text-secondary">Confirm Password</label>
            <input
              id="reg-confirm"
              type="password"
              name="confirmPassword"
              value={formData.confirmPassword}
              onChange={handleChange}
              placeholder="Re-enter your password"
              className={inputClass('confirmPassword')}
            />
            {errors.confirmPassword && <span className="text-xs text-error">⚠ {errors.confirmPassword}</span>}
          </div>

          {/* Submit */}
          <button
            type="submit"
            disabled={loading}
            className="w-full py-3.5 mt-2 btn-gradient text-white font-semibold rounded-xl text-base
              transition-all duration-200 cursor-pointer disabled:opacity-50 disabled:cursor-not-allowed
              flex items-center justify-center gap-2 border-none"
          >
            {loading ? (
              <>
                <span className="spinner w-5 h-5" />
                Creating Account...
              </>
            ) : (
              'Create Account'
            )}
          </button>
        </form>

        {/* Footer */}
        <p className="text-center mt-6 text-sm text-text-secondary">
          Already have an account?{' '}
          <Link to="/login" className="font-semibold text-accent hover:text-accent-hover no-underline">
            Sign In
          </Link>
        </p>
      </div>
    </div>
  );
}
