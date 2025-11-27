'use client';

import { useState } from 'react';
import DashboardLayout from '../components/DashboardLayout';
import { Users, Search, FileText, Activity, ChevronRight, Plus, Loader2 } from 'lucide-react';
import { api } from '../utils/api';

export default function PatientsPage() {
    // Mock patients for now, as we extract them from documents usually
    const [patients, setPatients] = useState([
        { id: 'PAT001', name: 'Sarah Connor', age: 45, gender: 'F', lastVisit: '2024-03-15', status: 'Critical', condition: 'Hypertension' },
        { id: 'PAT002', name: 'John Doe', age: 32, gender: 'M', lastVisit: '2024-03-10', status: 'Stable', condition: 'Routine Checkup' },
        { id: 'PAT003', name: 'Emily Blunt', age: 28, gender: 'F', lastVisit: '2024-02-28', status: 'Stable', condition: 'Migraine' },
        { id: 'PAT004', name: 'Michael Scott', age: 54, gender: 'M', lastVisit: '2024-02-15', status: 'Warning', condition: 'Diabetes T2' },
    ]);

    const [generatingId, setGeneratingId] = useState<string | null>(null);

    const handleGenerateSummary = async (patientId: string) => {
        setGeneratingId(patientId);
        try {
            const summary = await api.generateSummary(patientId);
            alert(`Summary generated for ${patientId}:\n\n${summary.summary || 'No summary available.'}`);
        } catch (error) {
            console.error("Error generating summary", error);
            alert("Failed to generate summary. Make sure backend is running.");
        } finally {
            setGeneratingId(null);
        }
    };

    return (
        <DashboardLayout>
            <div className="space-y-6">
                <div className="flex justify-between items-center">
                    <div>
                        <h1 className="text-3xl font-bold text-gray-900">Patients</h1>
                        <p className="text-gray-600 mt-1">Manage patient records and generate summaries</p>
                    </div>
                    <button className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors flex items-center space-x-2">
                        <Plus className="h-5 w-5" />
                        <span>Add Patient</span>
                    </button>
                </div>

                {/* Search & Filter */}
                <div className="bg-white p-4 rounded-xl shadow-sm border border-gray-100 flex space-x-4">
                    <div className="flex-1 relative">
                        <Search className="absolute left-3 top-2.5 h-5 w-5 text-gray-400" />
                        <input
                            type="text"
                            placeholder="Search patients by name, ID, or condition..."
                            className="w-full pl-10 pr-4 py-2 rounded-lg border border-gray-200 focus:border-blue-500 focus:ring-2 focus:ring-blue-200 outline-none"
                        />
                    </div>
                    <select className="border border-gray-200 rounded-lg px-4 py-2 text-gray-600 outline-none focus:border-blue-500">
                        <option>All Statuses</option>
                        <option>Critical</option>
                        <option>Stable</option>
                        <option>Warning</option>
                    </select>
                </div>

                {/* Patients List */}
                <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
                    <table className="min-w-full divide-y divide-gray-200">
                        <thead className="bg-gray-50">
                            <tr>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Patient</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Last Visit</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Condition</th>
                                <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                            </tr>
                        </thead>
                        <tbody className="bg-white divide-y divide-gray-200">
                            {patients.map((patient) => (
                                <tr key={patient.id} className="hover:bg-gray-50 transition-colors cursor-pointer group">
                                    <td className="px-6 py-4 whitespace-nowrap">
                                        <div className="flex items-center">
                                            <div className="flex-shrink-0 h-10 w-10 rounded-full bg-gradient-to-br from-blue-500 to-teal-500 flex items-center justify-center text-white font-medium">
                                                {patient.name.charAt(0)}
                                            </div>
                                            <div className="ml-4">
                                                <div className="text-sm font-medium text-gray-900">{patient.name}</div>
                                                <div className="text-sm text-gray-500">{patient.age} yrs • {patient.gender}</div>
                                            </div>
                                        </div>
                                    </td>
                                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                        {patient.id}
                                    </td>
                                    <td className="px-6 py-4 whitespace-nowrap">
                                        <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${patient.status === 'Critical' ? 'bg-red-100 text-red-800' :
                                                patient.status === 'Warning' ? 'bg-yellow-100 text-yellow-800' :
                                                    'bg-green-100 text-green-800'
                                            }`}>
                                            {patient.status}
                                        </span>
                                    </td>
                                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                        {patient.lastVisit}
                                    </td>
                                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                        {patient.condition}
                                    </td>
                                    <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                                        <div className="flex justify-end space-x-2 opacity-0 group-hover:opacity-100 transition-opacity">
                                            <button
                                                onClick={(e) => { e.stopPropagation(); handleGenerateSummary(patient.id); }}
                                                className="text-blue-600 hover:text-blue-900 bg-blue-50 p-2 rounded-lg flex items-center"
                                                title="Generate Summary"
                                                disabled={generatingId === patient.id}
                                            >
                                                {generatingId === patient.id ? <Loader2 className="h-4 w-4 animate-spin" /> : <FileText className="h-4 w-4" />}
                                            </button>
                                            <button className="text-teal-600 hover:text-teal-900 bg-teal-50 p-2 rounded-lg">
                                                <Activity className="h-4 w-4" />
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </DashboardLayout>
    );
}
