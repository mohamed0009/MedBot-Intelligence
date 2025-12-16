import React from 'react';
import { LucideIcon } from 'lucide-react';

interface FormInputProps extends React.InputHTMLAttributes<HTMLInputElement> {
    label?: string;
    error?: string;
    helperText?: string;
    icon?: LucideIcon;
    iconPosition?: 'left' | 'right';
}

export const FormInput: React.FC<FormInputProps> = ({
    label,
    error,
    helperText,
    icon: Icon,
    iconPosition = 'left',
    className = '',
    ...props
}) => {
    const hasError = !!error;

    return (
        <div className="w-full">
            {label && (
                <label className="block text-sm font-medium text-gray-700 mb-1">
                    {label}
                </label>
            )}
            <div className="relative">
                {Icon && iconPosition === 'left' && (
                    <div className="absolute left-3 top-1/2 transform -translate-y-1/2">
                        <Icon className="h-5 w-5 text-gray-400" />
                    </div>
                )}
                <input
                    className={`
            w-full px-4 py-2 rounded-lg border transition-all
            ${Icon && iconPosition === 'left' ? 'pl-10' : ''}
            ${Icon && iconPosition === 'right' ? 'pr-10' : ''}
            ${hasError
                            ? 'border-red-300 focus:border-red-500 focus:ring-red-500'
                            : 'border-gray-300 focus:border-blue-600 focus:ring-blue-600'
                        }
            focus:outline-none focus:ring-2 focus:ring-opacity-20
            disabled:bg-gray-50 disabled:text-gray-500 disabled:cursor-not-allowed
            ${className}
          `}
                    {...props}
                />
                {Icon && iconPosition === 'right' && (
                    <div className="absolute right-3 top-1/2 transform -translate-y-1/2">
                        <Icon className="h-5 w-5 text-gray-400" />
                    </div>
                )}
            </div>
            {error && (
                <p className="mt-1 text-sm text-red-600">{error}</p>
            )}
            {helperText && !error && (
                <p className="mt-1 text-sm text-gray-500">{helperText}</p>
            )}
        </div>
    );
};
