import React from 'react';
import { LucideIcon, ArrowUpRight, ArrowDownRight } from 'lucide-react';

interface StatCardProps {
    label: string;
    value: string | number;
    change?: {
        value: string;
        trend: 'up' | 'down';
    };
    icon: LucideIcon;
    color: 'blue' | 'green' | 'purple' | 'teal';
}

export const StatCard: React.FC<StatCardProps> = ({
    label,
    value,
    change,
    icon: Icon,
    color
}) => {
    const colorClasses = {
        blue: 'from-blue-500 to-blue-600',
        green: 'from-green-500 to-green-600',
        purple: 'from-purple-500 to-purple-600',
        teal: 'from-teal-500 to-teal-600'
    };

    return (
        <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100 hover:shadow-md transition-all duration-300 hover:-translate-y-1">
            <div className="flex items-center justify-between">
                <div className="flex-1">
                    <p className="text-sm font-medium text-gray-600">{label}</p>
                    <p className="text-3xl font-bold text-gray-900 mt-2">{value}</p>
                    {change && (
                        <div className="flex items-center mt-2">
                            {change.trend === 'up' ? (
                                <ArrowUpRight className="h-3 w-3 text-green-600 mr-1" />
                            ) : (
                                <ArrowDownRight className="h-3 w-3 text-red-600 mr-1" />
                            )}
                            <span className={`text-sm font-medium ${change.trend === 'up' ? 'text-green-600' : 'text-red-600'}`}>
                                {change.value}
                            </span>
                        </div>
                    )}
                </div>
                <div className={`p-3 rounded-xl bg-gradient-to-br ${colorClasses[color]} shadow-lg flex-shrink-0`}>
                    <Icon className="h-6 w-6 text-white" />
                </div>
            </div>
        </div>
    );
};
