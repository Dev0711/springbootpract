import { Link } from 'react-router-dom';

export default function Footer() {
  return (
    <footer className="border-t border-border-subtle bg-bg-primary/50 pt-12 pb-8">
      <div className="w-full max-w-[1200px] mx-auto px-6">
        {/* Grid */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-10 mb-10">
          {/* Brand */}
          <div>
            <div className="flex items-center gap-2 mb-4">
              <span className="w-8 h-8 btn-gradient rounded-lg flex items-center justify-center text-base">⚡</span>
              <span className="gradient-text text-lg font-extrabold">EcomAce</span>
            </div>
            <p className="text-text-muted text-sm leading-relaxed max-w-[300px]">
              Your premium destination for quality products. Experience seamless shopping with cutting-edge technology.
            </p>
          </div>

          {/* Shop */}
          <div>
            <h4 className="text-sm font-bold uppercase tracking-widest text-text-primary mb-4">Shop</h4>
            <ul className="flex flex-col gap-3 list-none">
              <li><Link to="/" className="text-text-muted text-sm hover:text-accent transition-colors no-underline">Electronics</Link></li>
              <li><Link to="/" className="text-text-muted text-sm hover:text-accent transition-colors no-underline">Fashion</Link></li>
              <li><Link to="/" className="text-text-muted text-sm hover:text-accent transition-colors no-underline">Home & Living</Link></li>
              <li><Link to="/" className="text-text-muted text-sm hover:text-accent transition-colors no-underline">Sports</Link></li>
            </ul>
          </div>

          {/* Company */}
          <div>
            <h4 className="text-sm font-bold uppercase tracking-widest text-text-primary mb-4">Company</h4>
            <ul className="flex flex-col gap-3 list-none">
              <li><Link to="/" className="text-text-muted text-sm hover:text-accent transition-colors no-underline">About Us</Link></li>
              <li><Link to="/" className="text-text-muted text-sm hover:text-accent transition-colors no-underline">Careers</Link></li>
              <li><Link to="/" className="text-text-muted text-sm hover:text-accent transition-colors no-underline">Blog</Link></li>
              <li><Link to="/" className="text-text-muted text-sm hover:text-accent transition-colors no-underline">Contact</Link></li>
            </ul>
          </div>

          {/* Support */}
          <div>
            <h4 className="text-sm font-bold uppercase tracking-widest text-text-primary mb-4">Support</h4>
            <ul className="flex flex-col gap-3 list-none">
              <li><Link to="/" className="text-text-muted text-sm hover:text-accent transition-colors no-underline">Help Center</Link></li>
              <li><Link to="/" className="text-text-muted text-sm hover:text-accent transition-colors no-underline">Privacy Policy</Link></li>
              <li><Link to="/" className="text-text-muted text-sm hover:text-accent transition-colors no-underline">Terms of Service</Link></li>
              <li><Link to="/" className="text-text-muted text-sm hover:text-accent transition-colors no-underline">Returns</Link></li>
            </ul>
          </div>
        </div>

        {/* Bottom */}
        <div className="border-t border-border-subtle pt-6 flex flex-col sm:flex-row items-center justify-between gap-4 text-xs text-text-muted">
          <p>© 2026 EcomAce. All rights reserved.</p>
          <p>Built with ❤️ for learning</p>
        </div>
      </div>
    </footer>
  );
}
