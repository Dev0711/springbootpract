import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const categories = [
  { icon: '💻', name: 'Electronics', count: '2,500+ Products', gradient: 'from-violet-600/20 to-cyan-600/20' },
  { icon: '👗', name: 'Fashion', count: '5,000+ Products', gradient: 'from-pink-600/20 to-orange-600/20' },
  { icon: '🏠', name: 'Home & Living', count: '3,200+ Products', gradient: 'from-emerald-600/20 to-teal-600/20' },
  { icon: '⚽', name: 'Sports', count: '1,800+ Products', gradient: 'from-amber-600/20 to-red-600/20' },
];

const features = [
  { icon: '🚀', title: 'Lightning Fast', description: 'Experience blazing fast page loads and seamless checkout with our optimized platform.' },
  { icon: '🔒', title: 'Secure Payments', description: 'Your transactions are protected with bank-level encryption and advanced fraud detection.' },
  { icon: '📦', title: 'Free Shipping', description: 'Enjoy free shipping on all orders over ₹499. Fast delivery guaranteed within 3-5 days.' },
  { icon: '🔄', title: 'Easy Returns', description: '30-day hassle-free return policy. No questions asked, full refund guaranteed.' },
  { icon: '💬', title: '24/7 Support', description: 'Our dedicated support team is available around the clock to help you with anything.' },
  { icon: '⭐', title: 'Top Quality', description: 'Every product is quality-checked and sourced from verified, premium sellers.' },
];

const stats = [
  { value: '50K+', label: 'Happy Customers' },
  { value: '12K+', label: 'Products' },
  { value: '99.9%', label: 'Uptime' },
  { value: '4.9★', label: 'App Rating' },
];

export default function Home() {
  const { isAuthenticated, user } = useAuth();

  return (
    <div className="animate-fade-in">
      {/* ===== Hero Section ===== */}
      <section className="relative pt-24 pb-16 md:pt-32 md:pb-20 text-center overflow-hidden">
        {/* Glow effect */}
        <div className="absolute top-[-50%] left-1/2 -translate-x-1/2 w-[800px] h-[800px] bg-[radial-gradient(circle,rgba(139,92,246,0.12)_0%,transparent_70%)] pointer-events-none" />

        <div className="w-full max-w-[1200px] mx-auto px-6 relative">
          {/* Badge */}
          <div className="animate-fade-in-up">
            <span className="inline-flex items-center gap-2 px-4 py-2 glass rounded-full text-xs font-semibold text-accent uppercase tracking-wide mb-6">
              <span className="w-1.5 h-1.5 rounded-full bg-success animate-[pulse-dot_2s_infinite]" />
              {isAuthenticated ? `Welcome back, ${user?.name || user?.firstName || 'friend'}!` : 'Now Live — Spring 2026 Collection'}
            </span>
          </div>

          {/* Headline */}
          <h1 className="animate-fade-in-up delay-1 text-[clamp(2.5rem,6vw,4.5rem)] font-black leading-[1.1] tracking-tight mb-6">
            Discover the Future of{' '}
            <span className="gradient-text">Online Shopping</span>
          </h1>

          {/* Subtitle */}
          <p className="animate-fade-in-up delay-2 text-lg text-text-secondary max-w-[600px] mx-auto mb-10 leading-relaxed">
            Curated collections, unbeatable prices, and a checkout experience so
            smooth you'll wonder how you ever shopped anywhere else.
          </p>

          {/* CTA buttons */}
          <div className="animate-fade-in-up delay-3 flex items-center justify-center gap-4 flex-wrap">
            {isAuthenticated ? (
              <Link
                to="/profile"
                className="inline-flex items-center gap-2 px-8 py-4 btn-gradient text-white font-semibold rounded-xl text-base no-underline transition-all duration-200"
              >
                View Profile →
              </Link>
            ) : (
              <>
                <Link
                  to="/register"
                  className="inline-flex items-center gap-2 px-8 py-4 btn-gradient text-white font-semibold rounded-xl text-base no-underline transition-all duration-200"
                >
                  Start Shopping →
                </Link>
                <Link
                  to="/login"
                  className="inline-flex items-center gap-2 px-8 py-4 glass glass-hover text-text-primary font-semibold rounded-xl text-base no-underline transition-all duration-200"
                >
                  Sign In
                </Link>
              </>
            )}
          </div>

          {/* Stats */}
          <div className="animate-fade-in-up delay-4 flex items-center justify-center gap-12 mt-16 pt-12 border-t border-border-subtle flex-wrap">
            {stats.map((stat) => (
              <div key={stat.label} className="text-center">
                <div className="text-3xl font-extrabold tracking-tight gradient-text">{stat.value}</div>
                <div className="text-sm text-text-muted mt-1">{stat.label}</div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ===== Categories Section ===== */}
      <section className="py-20">
        <div className="w-full max-w-[1200px] mx-auto px-6">
          <div className="text-center mb-12">
            <p className="text-xs font-bold uppercase tracking-[0.1em] text-accent mb-3">Browse</p>
            <h2 className="text-3xl font-extrabold tracking-tight mb-4">Shop by Category</h2>
            <p className="text-base text-text-secondary max-w-[500px] mx-auto">
              Explore our handpicked collections across every category you love.
            </p>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
            {categories.map((cat, i) => (
              <div
                key={cat.name}
                className={`animate-fade-in-up delay-${i + 1}
                  group relative rounded-2xl overflow-hidden aspect-[4/5] cursor-pointer
                  bg-gradient-to-br ${cat.gradient} border border-border-subtle
                  transition-all duration-300 hover:-translate-y-1 hover:shadow-2xl hover:border-border-hover`}
              >
                <div className="absolute inset-0 bg-gradient-to-t from-bg-primary/90 to-transparent" />
                <div className="absolute bottom-0 left-0 right-0 p-6">
                  <div className="text-4xl mb-3 transition-transform duration-500 group-hover:scale-110">
                    {cat.icon}
                  </div>
                  <h3 className="text-xl font-bold mb-1">{cat.name}</h3>
                  <p className="text-sm text-text-muted">{cat.count}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ===== Features Section ===== */}
      <section className="py-20">
        <div className="w-full max-w-[1200px] mx-auto px-6">
          <div className="text-center mb-12">
            <p className="text-xs font-bold uppercase tracking-[0.1em] text-accent mb-3">Why Us</p>
            <h2 className="text-3xl font-extrabold tracking-tight mb-4">
              Why Choose <span className="gradient-text">EcomAce</span>?
            </h2>
            <p className="text-base text-text-secondary max-w-[500px] mx-auto">
              We go above and beyond to deliver the best shopping experience.
            </p>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
            {features.map((feat, i) => (
              <div
                key={feat.title}
                className={`animate-fade-in-up delay-${i + 1}
                  group glass glass-hover rounded-2xl p-8 text-center
                  transition-all duration-300 hover:-translate-y-0.5 hover:shadow-lg`}
              >
                <div className="w-[60px] h-[60px] mx-auto mb-5 btn-gradient rounded-xl flex items-center justify-center text-2xl
                  transition-transform duration-500 group-hover:scale-110 group-hover:rotate-[5deg]">
                  {feat.icon}
                </div>
                <h3 className="text-lg font-bold mb-3">{feat.title}</h3>
                <p className="text-sm text-text-secondary leading-relaxed">{feat.description}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ===== CTA Section ===== */}
      {!isAuthenticated && (
        <section className="py-20">
          <div className="w-full max-w-[1200px] mx-auto px-6">
            <div className="relative glass rounded-3xl p-12 md:p-16 text-center overflow-hidden">
              <div className="absolute inset-0 bg-gradient-to-br from-accent/10 to-accent-cyan/10 pointer-events-none" />
              <div className="relative z-10">
                <h2 className="text-3xl md:text-4xl font-extrabold tracking-tight mb-4">
                  Ready to Get Started?
                </h2>
                <p className="text-lg text-text-secondary max-w-[500px] mx-auto mb-8 leading-relaxed">
                  Join thousands of happy shoppers and start discovering amazing products today.
                </p>
                <Link
                  to="/register"
                  className="inline-flex items-center gap-2 px-10 py-4 btn-gradient text-white font-semibold rounded-xl text-lg no-underline transition-all duration-200"
                >
                  Create Free Account →
                </Link>
              </div>
            </div>
          </div>
        </section>
      )}
    </div>
  );
}
