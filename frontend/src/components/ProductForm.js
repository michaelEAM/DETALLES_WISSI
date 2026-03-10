import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import './product-styles.css';

export default function ProductForm({ onAdd, categories }) {
  const navigate = useNavigate();
  const { idCategoria } = useParams();
  const [name, setName] = useState('');
  const [price, setPrice] = useState('');
  const [description, setDescription] = useState('');
  const [code, setCode] = useState('');
  const [stock, setStock] = useState('');
  const [category, setCategory] = useState(idCategoria || '');
  const [files, setFiles] = useState([]); // array of File objects (limited to 1)
  const [previews, setPreviews] = useState([]); // array of dataURLs for preview (limited to 1)

  // Set default category when categories load
  useEffect(() => {
    if (categories && categories.length > 0 && !category) {
      setCategory(categories[0].id.toString());
    }
  }, [categories, category]);

  function handleFiles(e) {
    const selectedFiles = Array.from(e.target.files || []);
    if (selectedFiles.length === 0) return;

    // Limit to one file
    const file = selectedFiles[0];
    const reader = new FileReader();
    reader.onload = ev => {
      setPreviews([ev.target.result]);
    };
    reader.readAsDataURL(file);
    setFiles([file]);
  }

  function removeImage() {
    setFiles([]);
    setPreviews([]);
  }

  async function handleSubmit(e) {
    e.preventDefault();
    if (!name.trim()) return alert('Nombre requerido');
    if (!code.trim()) return alert('Código requerido');
    if (!category || Number(category) <= 0) return alert('Categoría requerida');

    const formData = new FormData();
    const productData = {
      codigo: code.trim(),
      nombre: name.trim(),
      descripcion: description.trim(),
      precio: price ? Number(price) : 0,
      stock: stock ? Number(stock) : 0,
      id_categoria: Number(category)
    };
    formData.append('producto', JSON.stringify(productData));
    if (files.length > 0) {
      formData.append('imagen', files[0]);
    }

    try {
      if (onAdd) {
        await onAdd(formData);
        // reset only on success
        setName('');
        setPrice('');
        setDescription('');
        setCode('');
        setStock('');
        setCategory(idCategoria || (categories && categories.length > 0 ? categories[0].id.toString() : ''));
        setFiles([]);
        setPreviews([]);
      }
      // navigate back to list
      try { navigate('/'); } catch (e) { /* ignore */ }
    } catch (error) {
      console.error('Error adding product:', error);
      alert('Error al agregar producto');
    }
  }

  return (
    <form className="product-form" onSubmit={handleSubmit}>
      <div className="form-row">
        <label>Nombre</label>
        <input value={name} onChange={e => setName(e.target.value)} placeholder="Nombre del producto" />
      </div>

      <div className="form-row">
        <label>Precio</label>
        <input type="number" value={price} onChange={e => setPrice(e.target.value)} placeholder="0.00" />
      </div>

      <div className="form-row">
        <label>Descripción</label>
        <textarea value={description} onChange={e => setDescription(e.target.value)} placeholder="Descripción corta" />
      </div>

      <div className="form-row">
        <label>Código</label>
        <input value={code} onChange={e => setCode(e.target.value)} placeholder="Código del producto" />
      </div>

      <div className="form-row">
        <label>Stock</label>
        <input type="number" value={stock} onChange={e => setStock(e.target.value)} placeholder="0" />
      </div>

      <div className="form-row">
        <label>Categoría</label>
        <select value={category} onChange={e => setCategory(e.target.value)}>
          <option value="">Seleccionar categoría</option>
          {categories && categories.map(cat => (
            <option key={cat.id} value={cat.id}>{cat.name}</option>
          ))}
        </select>
      </div>

      <div className="form-row">
        <label>Imágenes</label>
        <input type="file" accept="image/*" multiple onChange={handleFiles} />
        <div className="preview-list">
          {previews.map((src, i) => (
            <div key={i} className="preview-item">
              <img src={src} alt={`preview-${i}`} />
              <button type="button" className="preview-remove" onClick={() => removeImage(i)}>x</button>
            </div>
          ))}
        </div>
      </div>

      <div className="form-actions">
        <button type="submit" className="btn-primary">Agregar producto</button>
      </div>
    </form>
  );
}
