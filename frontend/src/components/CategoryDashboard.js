import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './category-styles.css';

function emptyCategory(name, description) {
  return { name: name || `Categoria ${new Date().getTime()}`, description: description || '' };
}

export default function CategoryDashboard({ categories, onAddCategory, onDeleteCategory }) {
  const navigate = useNavigate();
  const [creating, setCreating] = useState(false);
  const [newName, setNewName] = useState(''); 
  const [newDescription, setNewDescription] = useState('');

  async function handleAddCategory() {
    if (!newName.trim()) return alert('Nombre de categoría requerido');
    const cat = emptyCategory(newName.trim(), newDescription.trim());
    const newCategoryId = await onAddCategory(cat);
    setNewName('');
    setNewDescription('');
    setCreating(false);
    if (newCategoryId) {
      navigate(`/category/${newCategoryId}`);
    }
  }

  function handleDeleteCategory(id) {
    // eslint-disable-next-line no-restricted-globals
    if (!confirm('Eliminar categoría y todos sus productos?')) return;
    onDeleteCategory(id);
  }

  function handleCategoryClick(cat) {
    navigate(`/category/${cat.id}`);
  }

  return (
    <div className="category-dashboard">
      <div className="category-header">
        <h2>Dashboard de Categorías</h2>
        <div className="category-actions">
          {!creating && <button className="btn-primary" onClick={() => setCreating(true)}>Agregar tipo de producto</button>}
        </div>
      </div>

      {creating && (
        <div className="create-row">
          <input value={newName} onChange={e => setNewName(e.target.value)} placeholder="Nombre de la categoría (p. ej. Peluches)" />
          <div className="create-actions">
            <button className="btn-primary" onClick={handleAddCategory}>Crear</button>
            <button onClick={() => { setCreating(false); setNewName(''); }}>Cancelar</button>
          </div>
        </div>
      )}

      <div className="category-grid">
        {categories.length === 0 && <div className="empty">No hay categorías aún. Crea una usando "Agregar tipo de producto".</div>}

        {categories.map(cat => (
          <div className="category-card-simple" key={cat.id} onClick={() => handleCategoryClick(cat)}>
            <h3>{cat.name}</h3>
            <div className="category-count">Productos: {cat.products?.length || 0}</div>
          </div>
        ))}
      </div>
    </div>
  );
}
