'use client';

import { useState, useEffect, useCallback } from 'react';
import DashboardLayout from '../components/DashboardLayout';
import { Upload, FileText, CheckCircle, Clock, AlertCircle, Trash2, Download, Eye, RefreshCw } from 'lucide-react';
import { api } from '../utils/api';

interface Document {
    id: string;
    filename: string;
    content_type: string;
    size: number;
    status: string;
    created_at: string;
    metadata?: any;
}

export default function DocumentsPage() {
    const [documents, setDocuments] = useState<Document[]>([]);
    const [loading, setLoading] = useState(true);
    const [uploading, setUploading] = useState(false);
    const [dragActive, setDragActive] = useState(false);

    const fetchDocuments = async () => {
        try {
            setLoading(true);
            const data = await api.getDocuments();
            // Handle different response structures (list or object with items)
            const docs = Array.isArray(data) ? data : (data.items || []);
            setDocuments(docs);
        } catch (error) {
            console.error("Failed to fetch documents", error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchDocuments();
    }, []);

    const handleDrag = useCallback((e: React.DragEvent) => {
        e.preventDefault();
        e.stopPropagation();
        if (e.type === 'dragenter' || e.type === 'dragover') {
            setDragActive(true);
        } else if (e.type === 'dragleave') {
            setDragActive(false);
        }
    }, []);

    const handleDrop = useCallback(async (e: React.DragEvent) => {
        e.preventDefault();
        e.stopPropagation();
        setDragActive(false);

        if (e.dataTransfer.files && e.dataTransfer.files[0]) {
            await handleUpload(e.dataTransfer.files[0]);
        }
    }, []);

    const handleFileInput = useCallback(async (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files[0]) {
            await handleUpload(e.target.files[0]);
        }
    }, []);

    const handleUpload = async (file: File) => {
        setUploading(true);
        try {
            await api.uploadDocument(file);
            await fetchDocuments(); // Refresh list
        } catch (error) {
            console.error('Upload error:', error);
            alert('Failed to upload document');
        } finally {
            setUploading(false);
        }
    };

    const handleDelete = async (id: string) => {
        if (confirm('Are you sure you want to delete this document?')) {
            try {
                await api.deleteDocument(id);
                setDocuments(prev => prev.filter(d => d.id !== id));
            } catch (error) {
                console.error('Delete error:', error);
                alert('Failed to delete document');
            }
        }
    };

    const formatSize = (bytes: number) => {
        if (bytes === 0) return '0 B';
        const k = 1024;
        const sizes = ['B', 'KB', 'MB', 'GB'];
        const i = Math.floor(Math.log(bytes) / Math.log(k));
        return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
    };

    return (
        <DashboardLayout>
            <div className="space-y-6">
                <div className="flex justify-between items-center">
                    <div>
                        <h1 className="text-3xl font-bold text-gray-900">Documents</h1>
                        <p className="text-gray-600 mt-1">Manage and process medical documents</p>
                    </div>
                    <button
                        onClick={fetchDocuments}
                        className="p-2 text-gray-500 hover:text-blue-600 hover:bg-blue-50 rounded-lg transition-colors"
                        title="Refresh list"
                    >
                        <RefreshCw className={`h-5 w-5 ${loading ? 'animate-spin' : ''}`} />
                    </button>
                </div>

                {/* Upload Area */}
                <div
                    className={`relative border-2 border-dashed rounded-2xl p-12 text-center transition-all ${dragActive ? 'border-blue-600 bg-blue-50' : 'border-gray-300 bg-white hover:border-gray-400'
                        }`}
                    onDragEnter={handleDrag}
                    onDragLeave={handleDrag}
                    onDragOver={handleDrag}
                    onDrop={handleDrop}
                >
                    <input
                        type="file"
                        id="file-upload"
                        className="sr-only"
                        onChange={handleFileInput}
                        accept=".pdf,.docx,.hl7,.json,.txt"
                    />
                    <label htmlFor="file-upload" className="cursor-pointer">
                        <div className="space-y-4">
                            <div className="mx-auto w-16 h-16 bg-gradient-to-br from-blue-600 to-blue-700 rounded-xl flex items-center justify-center shadow-lg">
                                <Upload className="h-8 w-8 text-white" />
                            </div>
                            <div>
                                <p className="text-lg font-semibold text-gray-900">
                                    {dragActive ? 'Drop files here' : 'Drag & drop files here'}
                                </p>
                                <p className="text-sm text-gray-600 mt-1">or <span className="text-blue-600 font-medium">browse</span> to upload</p>
                            </div>
                            <div className="flex items-center justify-center space-x-6 text-xs text-gray-500">
                                <div className="flex items-center space-x-2">
                                    <FileText className="h-4 w-4" />
                                    <span>PDF, DOCX, HL7, FHIR</span>
                                </div>
                                <span>•</span>
                                <span>Max 100MB</span>
                            </div>
                        </div>
                    </label>
                    {uploading && (
                        <div className="absolute inset-0 bg-white/80 backdrop-blur-sm flex items-center justify-center rounded-2xl">
                            <div className="text-center">
                                <div className="w-16 h-16 border-4 border-blue-600 border-t-transparent rounded-full animate-spin mx-auto mb-4"></div>
                                <p className="text-sm font-medium text-gray-900">Uploading & Processing...</p>
                            </div>
                        </div>
                    )}
                </div>

                {/* Documents List */}
                <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
                    {documents.length === 0 && !loading ? (
                        <div className="p-12 text-center text-gray-500">
                            <FileText className="h-12 w-12 mx-auto mb-4 text-gray-300" />
                            <p>No documents found</p>
                        </div>
                    ) : (
                        <table className="min-w-full divide-y divide-gray-200">
                            <thead className="bg-gray-50">
                                <tr>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Name</th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Size</th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Date</th>
                                    <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                                </tr>
                            </thead>
                            <tbody className="bg-white divide-y divide-gray-200">
                                {documents.map((doc) => (
                                    <tr key={doc.id} className="hover:bg-gray-50 transition-colors">
                                        <td className="px-6 py-4 whitespace-nowrap">
                                            <div className="flex items-center">
                                                <FileText className="h-5 w-5 text-gray-400 mr-3" />
                                                <div className="text-sm font-medium text-gray-900">{doc.filename}</div>
                                            </div>
                                        </td>
                                        <td className="px-6 py-4 whitespace-nowrap">
                                            {doc.status === 'completed' ? (
                                                <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800">
                                                    <CheckCircle className="h-3 w-3 mr-1" /> Completed
                                                </span>
                                            ) : doc.status === 'processing' ? (
                                                <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800">
                                                    <Clock className="h-3 w-3 mr-1 animate-spin" /> Processing
                                                </span>
                                            ) : (
                                                <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-red-100 text-red-800">
                                                    <AlertCircle className="h-3 w-3 mr-1" /> Failed
                                                </span>
                                            )}
                                        </td>
                                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                            {formatSize(doc.size)}
                                        </td>
                                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                            {new Date(doc.created_at).toLocaleDateString()}
                                        </td>
                                        <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                                            <div className="flex justify-end space-x-2">
                                                <button className="text-gray-400 hover:text-blue-600 p-1">
                                                    <Eye className="h-4 w-4" />
                                                </button>
                                                <button className="text-gray-400 hover:text-green-600 p-1">
                                                    <Download className="h-4 w-4" />
                                                </button>
                                                <button
                                                    onClick={() => handleDelete(doc.id)}
                                                    className="text-gray-400 hover:text-red-600 p-1"
                                                >
                                                    <Trash2 className="h-4 w-4" />
                                                </button>
                                            </div>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    )}
                </div>
            </div>
        </DashboardLayout>
    );
}
