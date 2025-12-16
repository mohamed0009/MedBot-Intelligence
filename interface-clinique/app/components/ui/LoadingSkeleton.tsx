import React from 'react';

export const SkeletonCard: React.FC = () => (
    <div className="bg-white rounded-xl p-6 border border-gray-100 animate-pulse">
        <div className="flex items-center justify-between">
            <div className="flex-1">
                <div className="h-4 bg-gray-200 rounded w-1/4 mb-4"></div>
                <div className="h-8 bg-gray-200 rounded w-1/2 mb-2"></div>
                <div className="h-3 bg-gray-200 rounded w-1/3"></div>
            </div>
            <div className="w-12 h-12 bg-gray-200 rounded-xl"></div>
        </div>
    </div>
);

export const SkeletonTable: React.FC<{ rows?: number }> = ({ rows = 5 }) => (
    <div className="bg-white rounded-xl border border-gray-100 overflow-hidden">
        <div className="bg-gray-50 p-4">
            <div className="h-4 bg-gray-200 rounded w-1/4"></div>
        </div>
        {Array.from({ length: rows }).map((_, i) => (
            <div key={i} className="p-4 border-t border-gray-100 animate-pulse">
                <div className="flex items-center space-x-4">
                    <div className="h-10 w-10 bg-gray-200 rounded-full"></div>
                    <div className="flex-1 space-y-2">
                        <div className="h-4 bg-gray-200 rounded w-3/4"></div>
                        <div className="h-3 bg-gray-200 rounded w-1/2"></div>
                    </div>
                </div>
            </div>
        ))}
    </div>
);

export const SkeletonChat: React.FC = () => (
    <div className="space-y-6">
        {[1, 2].map((i) => (
            <div key={i} className="animate-pulse">
                <div className={`flex ${i % 2 === 0 ? 'justify-end' : 'justify-start'}`}>
                    <div className={`max-w-3xl rounded-2xl p-4 ${i % 2 === 0 ? 'bg-blue-100' : 'bg-gray-100'}`}>
                        <div className="h-4 bg-gray-300 rounded w-64 mb-2"></div>
                        <div className="h-4 bg-gray-300 rounded w-48"></div>
                    </div>
                </div>
            </div>
        ))}
    </div>
);
