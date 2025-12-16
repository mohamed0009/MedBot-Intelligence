'use client';

import { useEffect, useState } from 'react';
import DashboardLayout from './components/DashboardLayout';
import { FileText, CheckCircle, MessageSquare, Clock, TrendingUp, Activity } from 'lucide-react';
import { api } from './utils/api';
import { StatCard, Button, SkeletonCard } from './components/ui';

export default function Home() {
  const [stats, setStats] = useState({
    totalDocuments: 0,
    totalIndexed: 0,
    totalQueries: 0,
    processingSuccess: 0
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        setLoading(true);
        const data = await api.getStats();
        setStats(data);
        setError(null);
      } catch (err) {
        setError('Failed to load statistics');
        console.error('Error fetching stats:', err);
      } finally {
        setLoading(false);
      }
    };
    fetchStats();
  }, []);

  const statCards = [
    {
      name: 'Total Documents',
      value: stats.totalDocuments.toString(),
      change: '+12%',
      changeType: 'positive',
      icon: FileText,
      color: 'from-blue-500 to-blue-600',
    },
    {
      name: 'Indexed Chunks',
      value: stats.totalIndexed.toString(),
      change: '+100%',
      changeType: 'positive',
      icon: CheckCircle,
      color: 'from-green-500 to-green-600',
    },
    {
      name: 'AI Queries',
      value: stats.totalQueries.toString(),
      change: '+18%',
      changeType: 'positive',
      icon: MessageSquare,
      color: 'from-purple-500 to-purple-600',
    },
    {
      name: 'Avg Response Time',
      value: '1.8s',
      change: '-0.5s',
      changeType: 'positive',
      icon: Clock,
      color: 'from-teal-500 to-teal-600',
    },
  ];

  return (
    <DashboardLayout>
      <div className="space-y-6">
        {/* Welcome Section - UPGRADED */}
        <div className="relative bg-gradient-to-br from-blue-600 via-blue-700 to-teal-600 rounded-3xl p-8 text-white shadow-2xl overflow-hidden border border-blue-500/20">
          {/* Animated Background Elements */}
          <div className="absolute top-0 right-0 -mr-16 -mt-16 w-80 h-80 bg-white/10 rounded-full blur-3xl animate-pulse"></div>
          <div className="absolute bottom-0 left-0 -ml-16 -mb-16 w-80 h-80 bg-teal-500/20 rounded-full blur-3xl"></div>
          <div className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 w-96 h-96 bg-blue-400/10 rounded-full blur-3xl"></div>

          <div className="relative z-10">
            <div className="flex items-start justify-between mb-6">
              <div className="flex-1">
                <div className="flex items-center space-x-3 mb-3">
                  <div className="w-16 h-16 bg-white/20 backdrop-blur-lg rounded-2xl flex items-center justify-center border border-white/30">
                    <Activity className="h-8 w-8 text-white" />
                  </div>
                  <div>
                    <h1 className="text-4xl font-bold mb-1">Welcome back, Dr. Martinez! 👋</h1>
                    <p className="text-blue-100 text-lg">
                      Your AI-powered medical assistant is ready to help you analyze patient data
                    </p>
                  </div>
                </div>

                {/* Quick Stats Badges */}
                <div className="flex flex-wrap gap-3 mt-6">
                  <div className="bg-white/20 backdrop-blur-lg border border-white/30 rounded-xl px-4 py-2 flex items-center space-x-2">
                    <div className="w-2 h-2 bg-green-400 rounded-full animate-pulse"></div>
                    <span className="text-sm font-medium">System Online</span>
                  </div>
                  <div className="bg-white/20 backdrop-blur-lg border border-white/30 rounded-xl px-4 py-2 flex items-center space-x-2">
                    <Clock className="h-4 w-4" />
                    <span className="text-sm font-medium">Last sync: 2 mins ago</span>
                  </div>
                  <div className="bg-white/20 backdrop-blur-lg border border-white/30 rounded-xl px-4 py-2 flex items-center space-x-2">
                    <TrendingUp className="h-4 w-4" />
                    <span className="text-sm font-medium">Performance: Excellent</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Stats Grid */}
        <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-4">
          {loading ? (
            <>
              <SkeletonCard />
              <SkeletonCard />
              <SkeletonCard />
              <SkeletonCard />
            </>
          ) : (
            <>
              <StatCard
                label="Total Documents"
                value={stats.totalDocuments}
                change={{ value: "+12%", trend: "up" }}
                icon={FileText}
                color="blue"
              />
              <StatCard
                label="Indexed Chunks"
                value={stats.totalIndexed}
                change={{ value: "+100%", trend: "up" }}
                icon={CheckCircle}
                color="green"
              />
              <StatCard
                label="AI Queries"
                value={stats.totalQueries}
                change={{ value: "+18%", trend: "up" }}
                icon={MessageSquare}
                color="purple"
              />
              <StatCard
                label="Avg Response Time"
                value="1.8s"
                change={{ value: "-0.5s", trend: "up" }}
                icon={Clock}
                color="teal"
              />
            </>
          )}
        </div>

        {/* Recent Activity & Quick Actions */}
        <div className="grid grid-cols-1 gap-6 lg:grid-cols-3">
          {/* Recent Activity */}
          <div className="lg:col-span-2 bg-white rounded-xl shadow-sm border border-gray-100 p-6">
            <div className="flex items-center justify-between mb-6">
              <h2 className="text-xl font-bold text-gray-900">Recent Activity</h2>
              <button className="text-sm font-medium text-blue-600 hover:text-blue-700">
                View All
              </button>
            </div>
            <div className="space-y-4">
              {[
                { title: 'Patient PAT001 - Summary Generated', desc: 'Comprehensive medical summary created', time: '5 mins ago', icon: CheckCircle, color: 'text-green-600 bg-green-50' },
                { title: 'Document Uploaded', desc: 'Lab_Results_2024.pdf processing...', time: '12 mins ago', icon: Activity, color: 'text-blue-600 bg-blue-50' },
                { title: 'Q&A Session', desc: '5 questions answered about diabetes', time: '1 hour ago', icon: MessageSquare, color: 'text-purple-600 bg-purple-50' }
              ].map((activity, i) => (
                <div key={i} className="flex items-start space-x-4 p-4 rounded-lg hover:bg-gray-50 transition-colors border border-transparent hover:border-gray-100">
                  <div className={`p-2 rounded-lg ${activity.color}`}>
                    <activity.icon className="h-5 w-5" />
                  </div>
                  <div className="flex-1">
                    <h3 className="text-sm font-semibold text-gray-900">{activity.title}</h3>
                    <p className="text-sm text-gray-600 mt-1">{activity.desc}</p>
                    <p className="text-xs text-gray-400 mt-2">{activity.time}</p>
                  </div>
                </div>
              ))}
            </div>
          </div>

          {/* Quick Actions */}
          <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
            <h2 className="text-xl font-bold text-gray-900 mb-6">Quick Actions</h2>
            <div className="space-y-3">
              <Button variant="primary" size="lg" className="w-full">
                <FileText className="h-5 w-5 mr-2" />
                Upload Document
              </Button>
              <Button variant="primary" size="lg" className="w-full bg-gradient-to-r from-purple-600 to-purple-700 hover:from-purple-700 hover:to-purple-800">
                <MessageSquare className="h-5 w-5 mr-2" />
                Ask AI Question
              </Button>
              <Button variant="primary" size="lg" className="w-full bg-gradient-to-r from-teal-600 to-teal-700 hover:from-teal-700 hover:to-teal-800">
                <TrendingUp className="h-5 w-5 mr-2" />
                Generate Report
              </Button>
            </div>
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
}
