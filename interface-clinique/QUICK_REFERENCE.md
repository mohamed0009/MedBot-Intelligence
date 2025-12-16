# Quick Reference Guide
## Modern UI Components - MedBot Intelligence

---

## 🎨 Component Imports

```tsx
// Import all components at once
import { Button, StatusBadge, StatCard, SkeletonCard, SkeletonTable, FormInput } from './components/ui';

// Or import individually
import { Button } from './components/ui/Button';
import { StatusBadge } from './components/ui/StatusBadge';
```

---

## 🔘 Button Component

### Variants
```tsx
<Button variant="primary">Primary Action</Button>
<Button variant="secondary">Secondary</Button>
<Button variant="success">Save</Button>
<Button variant="danger">Delete</Button>
<Button variant="ghost">Cancel</Button>
```

### Sizes
```tsx
<Button size="sm">Small</Button>
<Button size="md">Medium (default)</Button>
<Button size="lg">Large</Button>
```

### With Icons
```tsx
import { Upload } from 'lucide-react';

<Button variant="primary">
  <Upload className="h-5 w-5 mr-2" />
  Upload File
</Button>
```

---

## 🏷️ StatusBadge Component

```tsx
<StatusBadge status="success">Completed</StatusBadge>
<StatusBadge status="processing">Processing</StatusBadge>
<StatusBadge status="warning">Pending</StatusBadge>
<StatusBadge status="error">Failed</StatusBadge>

// Without icon
<StatusBadge status="success" showIcon={false}>Done</StatusBadge>
```

---

## 📊 StatCard Component

```tsx
import { FileText } from 'lucide-react';

<StatCard
  label="Total Documents"
  value="1,234"
  change={{ value: "+12%", trend: "up" }}
  icon={FileText}
  color="blue"
/>

<StatCard
  label="Processing Time"
  value="1.8s"
  change={{ value: "-0.5s", trend: "down" }}
  icon={Clock}
  color="teal"
/>
```

### Available Colors
- `blue` - Primary actions, documents
- `green` - Success, health metrics
- `purple` - AI, intelligence features
- `teal` - Medical, calm indicators

---

## ⏳ Loading Skeletons

### Skeleton Card
```tsx
{loading ? (
  <SkeletonCard />
) : (
  <StatCard {...props} />
)}
```

### Skeleton Table
```tsx
{loading ? (
  <SkeletonTable rows={5} />
) : (
  <table>...</table>
)}
```

### Skeleton Chat
```tsx
{loading ? (
  <SkeletonChat />
) : (
  <div>Messages...</div>
)}
```

---

## 📝 FormInput Component

### Basic Input
```tsx
<FormInput
  label="Email Address"
  placeholder="Enter your email"
  type="email"
/>
```

### With Icon
```tsx
import { Search } from 'lucide-react';

<FormInput
  label="Search"
  placeholder="Search documents..."
  icon={Search}
  iconPosition="left"
/>
```

### With Error
```tsx
<FormInput
  label="Password"
  type="password"
  error="Password must be at least 8 characters"
/>
```

### With Helper Text
```tsx
<FormInput
  label="Username"
  helperText="Choose a unique username"
/>
```

---

## 🎨 Color System

### Primary Colors
```css
--primary-blue: #2563EB
--accent-teal: #0891B2
--ai-purple: #7C3AED
--success-green: #16A34A
--warning-yellow: #D97706
--error-red: #DC2626
```

### Usage in Tailwind
```tsx
className="bg-blue-600 text-white"
className="bg-teal-500 hover:bg-teal-600"
className="text-purple-600 bg-purple-50"
```

---

## 📐 Spacing Scale

```tsx
className="p-4"   // 16px padding
className="p-6"   // 24px padding
className="p-8"   // 32px padding
className="gap-6" // 24px gap
className="space-y-6" // 24px vertical spacing
```

---

## 🔄 Common Patterns

### Loading State Pattern
```tsx
const [loading, setLoading] = useState(true);
const [data, setData] = useState([]);

{loading ? (
  <SkeletonCard />
) : (
  <StatCard {...data} />
)}
```

### Error State Pattern
```tsx
const [error, setError] = useState<string | null>(null);

{error && (
  <div className="bg-red-50 border border-red-200 rounded-lg p-4">
    <p className="text-red-800">{error}</p>
  </div>
)}
```

### Action Buttons Pattern
```tsx
<div className="flex justify-end space-x-3">
  <Button variant="secondary">Cancel</Button>
  <Button variant="primary">Save Changes</Button>
</div>
```

---

## ♿ Accessibility

### ARIA Labels
```tsx
<button aria-label="Close dialog">
  <X className="h-5 w-5" />
</button>
```

### Focus States
```tsx
// Automatically applied via globals.css
*:focus-visible {
  outline: 2px solid #2563EB;
  outline-offset: 2px;
}
```

### Skip Navigation
```tsx
<a href="#main-content" className="sr-only focus:not-sr-only">
  Skip to main content
</a>
```

---

## 🎯 Quick Fixes

### Dynamic Tailwind Classes ❌
```tsx
// DON'T DO THIS
<div className={`bg-${color}-50`}>

// DO THIS INSTEAD
const colorClasses = {
  blue: 'bg-blue-50',
  green: 'bg-green-50'
};
<div className={colorClasses[color]}>
```

### Button Consistency
```tsx
// OLD
<button className="bg-blue-600 text-white px-4 py-2 rounded-lg">

// NEW
<Button variant="primary">Click Me</Button>
```

### Status Badges
```tsx
// OLD
<span className="bg-green-100 text-green-800 px-2 py-1 rounded-full">

// NEW
<StatusBadge status="success">Completed</StatusBadge>
```

---

## 🚀 Performance Tips

1. **Use Skeleton Loaders** - Better perceived performance
2. **Lazy Load Components** - Only load what's needed
3. **Memoize Expensive Calculations** - Use `useMemo`
4. **Debounce Input** - For search/filter inputs

---

## 📱 Responsive Breakpoints

```tsx
// Tailwind breakpoints
sm:  640px  // Small devices
md:  768px  // Tablets
lg:  1024px // Laptops
xl:  1280px // Desktops
2xl: 1536px // Large screens

// Usage
className="grid-cols-1 md:grid-cols-2 lg:grid-cols-4"
```

---

## 🎨 Gradient Examples

```tsx
// Button gradients
className="bg-gradient-to-r from-blue-600 to-blue-700"
className="bg-gradient-to-r from-purple-600 to-purple-700"
className="bg-gradient-to-r from-teal-600 to-teal-700"

// Background gradients
className="bg-gradient-to-br from-slate-50 via-blue-50 to-teal-50"
```

---

## 🔍 Common Issues & Solutions

### Issue: Component not found
**Solution:** Check import path
```tsx
import { Button } from './components/ui'; // ✅ Correct
import { Button } from './components/ui/Button'; // ✅ Also works
import { Button } from '../components/ui'; // ✅ Relative path
```

### Issue: Styles not applying
**Solution:** Ensure Tailwind classes are complete strings
```tsx
className="bg-blue-600" // ✅ Works
className={`bg-${color}-600`} // ❌ Won't work in production
```

### Issue: TypeScript errors
**Solution:** Check prop types
```tsx
<Button variant="primary"> // ✅ Correct
<Button variant="custom"> // ❌ Not a valid variant
```

---

**Need Help?** Check `IMPLEMENTATION_SUMMARY.md` for full documentation!
