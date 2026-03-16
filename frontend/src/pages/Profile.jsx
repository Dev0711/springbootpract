import { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { useToast } from '../components/Toast';
import { getUserByEmail } from '../api/api';

export default function Profile() {
  const { user, updateUserState } = useAuth();
  const toast = useToast();
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        setLoading(true);
        // Try fetching from user-service by email
        const email = user?.email;
        if (email) {
          const data = await getUserByEmail(email);
          setProfile(data);
        } else {
          // Fallback to local user data
          setProfile(user);
        }
      } catch (err) {
        console.error('Failed to load profile:', err);
        setError(err.message);
        // Fallback to locally stored user data
        setProfile(user);
      } finally {
        setLoading(false);
      }
    };

    fetchProfile();
  }, [user]);

  const getInitials = () => {
    const name = profile?.name || profile?.firstName || profile?.email || '';
    const parts = name.trim().split(/\s+/);
    if (parts.length >= 2) return (parts[0][0] + parts[1][0]).toUpperCase();
    return name.slice(0, 2).toUpperCase();
  };

  const displayName = profile?.name || `${profile?.firstName || ''} ${profile?.lastName || ''}`.trim() || profile?.email || 'User';

  if (loading) {
    return (
      <div className="min-h-[calc(100vh-72px)] flex items-center justify-center">
        <div className="spinner w-10 h-10 border-[3px]" />
      </div>
    );
  }

  return (
    <div className="animate-fade-in py-12">
      <div className="w-full max-w-[1200px] mx-auto px-6">
        {/* Header */}
        <div className="flex flex-col sm:flex-row items-center gap-8 mb-10 pb-10 border-b border-border-subtle">
          {/* Avatar */}
          <div className="w-[100px] h-[100px] rounded-full btn-gradient flex items-center justify-center text-4xl font-extrabold text-white shrink-0">
            {getInitials()}
          </div>

          {/* Info */}
          <div className="text-center sm:text-left">
            <h1 className="text-2xl font-extrabold mb-1">{displayName}</h1>
            <p className="text-sm text-text-secondary">{profile?.email || 'No email'}</p>
            <div className="flex items-center gap-2 mt-2 justify-center sm:justify-start flex-wrap">
              {profile?.userStatus && (
                <span className="inline-flex items-center gap-1 px-3 py-1 rounded-full text-xs font-semibold
                  bg-success/10 text-success border border-success/20">
                  ● {profile.userStatus}
                </span>
              )}
              {profile?.role && (
                <span className="inline-flex items-center gap-1 px-3 py-1 rounded-full text-xs font-semibold
                  bg-accent/10 text-accent border border-accent/20">
                  {profile.role}
                </span>
              )}
            </div>
          </div>
        </div>

        {/* Error banner */}
        {error && (
          <div className="mb-6 p-4 glass rounded-xl border-l-4 border-warning text-sm text-warning">
            ⚠ Could not fetch latest profile from server. Showing locally cached data.
          </div>
        )}

        {/* Details Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* Personal Info */}
          <div className="glass glass-hover rounded-2xl p-8 transition-all duration-300">
            <h2 className="text-lg font-bold mb-6 flex items-center gap-2">
              👤 Personal Information
            </h2>
            <div className="flex flex-col">
              <ProfileField label="Full Name" value={displayName} />
              <ProfileField label="Email" value={profile?.email || '—'} />
              <ProfileField label="Phone" value={profile?.phoneNumber || 'Not provided'} />
              <ProfileField label="Role" value={profile?.role || '—'} isLast />
            </div>
          </div>

          {/* Addresses */}
          <div className="glass glass-hover rounded-2xl p-8 transition-all duration-300">
            <h2 className="text-lg font-bold mb-6 flex items-center gap-2">
              📍 Addresses
            </h2>
            {profile?.addresses && profile.addresses.length > 0 ? (
              <div className="flex flex-col gap-4">
                {profile.addresses.map((addr, i) => (
                  <div key={i} className="p-4 bg-bg-card rounded-xl border border-border-subtle">
                    {addr.type && (
                      <span className="text-xs font-bold uppercase tracking-wider text-accent mb-2 block">
                        {addr.type}
                      </span>
                    )}
                    <p className="text-sm text-text-secondary leading-relaxed">
                      {[addr.street, addr.city, addr.state, addr.zipCode, addr.country]
                        .filter(Boolean)
                        .join(', ')}
                    </p>
                  </div>
                ))}
              </div>
            ) : (
              <div className="text-center py-8">
                <p className="text-4xl mb-3">🏠</p>
                <p className="text-sm text-text-muted">No addresses added yet</p>
              </div>
            )}
          </div>

          {/* Account Info */}
          <div className="glass glass-hover rounded-2xl p-8 transition-all duration-300 lg:col-span-2">
            <h2 className="text-lg font-bold mb-6 flex items-center gap-2">
              🔒 Account Details
            </h2>
            <div className="grid grid-cols-1 sm:grid-cols-3 gap-6">
              <div className="text-center p-4 bg-bg-card rounded-xl border border-border-subtle">
                <p className="text-2xl font-extrabold gradient-text">
                  {profile?.id ? profile.id.slice(0, 8) + '...' : '—'}
                </p>
                <p className="text-xs text-text-muted mt-1">User ID</p>
              </div>
              <div className="text-center p-4 bg-bg-card rounded-xl border border-border-subtle">
                <p className="text-2xl font-extrabold gradient-text">
                  {profile?.userStatus || '—'}
                </p>
                <p className="text-xs text-text-muted mt-1">Account Status</p>
              </div>
              <div className="text-center p-4 bg-bg-card rounded-xl border border-border-subtle">
                <p className="text-2xl font-extrabold gradient-text">
                  {profile?.addresses?.length || 0}
                </p>
                <p className="text-xs text-text-muted mt-1">Saved Addresses</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

function ProfileField({ label, value, isLast = false }) {
  return (
    <div className={`flex justify-between items-center py-3 ${isLast ? '' : 'border-b border-border-subtle'}`}>
      <span className="text-sm text-text-muted">{label}</span>
      <span className="text-sm font-medium">{value}</span>
    </div>
  );
}
