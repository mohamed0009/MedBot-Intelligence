import React from 'react';
import { CheckCircle, Clock, AlertTriangle, XCircle } from 'lucide-react';

interface StatusBadgeProps {
    status: 'success' | 'processing' | 'warning' | 'error';
    children: React.ReactNode;
    showIcon?: boolean;
}

export const StatusBadge: React.FC<StatusBadgeProps> = ({
    status,
    children,
    showIcon = true
}) => {
    const variants = {
        success: {
            className: 'bg-green-100 text-green-800',
            icon: CheckCircle
        },
        processing: {
            className: 'bg-blue-100 text-blue-800',
            icon: Clock
        },
        warning: {
            className: 'bg-yellow-100 text-yellow-800',
            icon: AlertTriangle
        },
        error: {
            className: 'bg-red-100 text-red-800',
            icon: XCircle
        }
    };

    const { className, icon: Icon } = variants[status];

    return (
        <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${className}`}>
            {showIcon && <Icon className="h-3 w-3 mr-1" />}
            {children}
        </span>
    );
};
