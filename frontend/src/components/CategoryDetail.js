import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import './category-styles.css';

export default function CategoryDetail({ categories, onAddProduct, onUpdateProduct, onDeleteProduct, onDeleteCategory }) {
  const { id } = useParams();
  const navigate = useNavigate();
  const [category, setCategory] = useState(null);
  const [modalProduct, setModalProduct] = useState(null); // {name, price, description, code, stock, images}
  const [editingProduct, setEditingProduct] = useState(null);
  const [modalSrc, setModalSrc] = useState(null);
  const [query, setQuery] = useState('');

  useEffect(() => {
    const cat = categories.find(c => String(c.id) === id);
    if (!cat) {
      navigate('/dashboard');
      return;
    }
    setCategory(cat);
  }, [id, categories, navigate]);

  function openAddProduct() {
    setModalProduct({ name: '', price: '', description: '', code: '', stock: '', files: [], previews: [] });
    setEditingProduct(null);
  }

  function openEditProduct(product) {
    setModalProduct({
      name: product.name,
      price: product.price,
      description: product.description,
      code: product.code,
      stock: product.stock,
      files: [],
      previews: product.images || []
    });
    setEditingProduct(product);
  }

  function closeProductModal() { setModalProduct(null); }

  function closeImageModal() { setModalSrc(null); }

  function handleDeleteProduct(prodId) {
    // eslint-disable-next-line no-restricted-globals
    if (!confirm('Eliminar este producto?')) return;
    onDeleteProduct(prodId);
  }

  function handleDeleteCategory() {
    // eslint-disable-next-line no-restricted-globals
    if (!confirm('Eliminar categoría y todos sus productos?')) return;
    onDeleteCategory(category.id);
    navigate('/dashboard');
  }

  function handleFileChange(files) {
    const arr = Array.from(files || []);
    setModalProduct(prev => ({ ...prev, files: arr }));
    arr.forEach(file => {
      const r = new FileReader();
      r.onload = e => setModalProduct(prev => ({ ...prev, previews: [e.target.result] }));
      r.readAsDataURL(file);
    });
  }

  function submitProduct(e) {
    e.preventDefault();
    const p = modalProduct;
    if (!p || !p.name.trim()) return alert('Nombre requerido');
    if (!p.code.trim()) return alert('Código requerido');

    // Verificación adicional: asegurar que category esté definido
    if (!category) return alert('Categoría no encontrada. Intenta recargar la página.');

    console.log("category:", category);
    console.log("category.id:", category.id);
    console.log("categories:", categories);

    const formData = new FormData();

    const productData = {
      codigo: p.code.trim(),
      nombre: p.name.trim(),
      descripcion: p.description.trim(),
      precio: p.price ? Number(p.price) : 0,
      stock: p.stock ? Number(p.stock) : 0,
      categoriaId: category.id
    };

    formData.append("producto", JSON.stringify(productData));

    if (p.files && p.files.length > 0) {
      formData.append("imagen", p.files[0]);
    }

    if (editingProduct) {
      onUpdateProduct(editingProduct.id, formData);
    } else {
      onAddProduct(formData);
    }
    closeProductModal();
  }

  if (!category) return <div>Cargando...</div>;

  const q = (query || '').trim().toLowerCase();
  const filteredProducts = q
    ? category.products.filter(p => {
        const searchableText = `${p.name} ${p.description} ${p.code} ${p.price} ${p.stock}`.toLowerCase();
        return searchableText.includes(q);
      })
    : category.products;

  return (
    <div className="category-detail">
      <div className="detail-header">
        <button className="btn-back" onClick={() => navigate('/dashboard')}>← Volver al Dashboard</button>
        <h2>{category.name}</h2>
        <div className="detail-actions">
          <button className="btn-primary" onClick={openAddProduct}>Agregar producto</button>
          <button className="btn-danger" onClick={handleDeleteCategory}>Eliminar categoría</button>
        </div>
      </div>

      <div className="detail-meta">
        <p>Productos: {category.products?.length || 0}</p>
      </div>

      <div className="product-list-header">
        <div className="search-bar">
          <input
            className="search-input"
            value={query}
            onChange={e => setQuery(e.target.value)}
            placeholder="Buscar productos en esta categoría..."
          />
          {query && <button className="search-clear" onClick={() => setQuery('')}>Limpiar</button>}
        </div>
        <div className="search-count">Mostrando {filteredProducts.length} de {category.products?.length || 0}</div>
      </div>

      <div className="product-grid">
        {filteredProducts && filteredProducts.length > 0 ? (
          filteredProducts.map(product => (
            <article className="product-card" key={product.id}>
              <div className="product-media">
                {product.images && product.images.length > 0 ? (
                  <img
                    src={product.images[0]}
                    alt={product.name}
                    className="product-main-img clickable"
                    onClick={() => setModalSrc(product.images[0])}
                  />
                ) : (
                  <div className="product-placeholder">Sin imagen</div>
                )}
              </div>

              <div className="product-body">
                <h3 className="product-title">{product.name}</h3>
                <p className="product-desc">{product.description}</p>
                <div className="product-meta">
                  <strong className="product-price">${product.price}</strong>
                  <button className="btn-edit" onClick={() => openEditProduct(product)}>Editar</button>
                  <button className="btn-delete" onClick={() => handleDeleteProduct(product.id)}>Eliminar</button>
                </div>

                {product.images && product.images.length > 1 && (
                  <div className="thumbs">
                    {product.images.map((src, i) => (
                      <img
                        key={i}
                        src={src}
                        alt={`${product.name} ${i}`}
                        className="thumb clickable"
                        onClick={() => setModalSrc(src)}
                      />
                    ))}
                  </div>
                )}
              </div>
            </article>
          ))
        ) : (
          <div className="empty">No hay productos en esta categoría. Agrega uno nuevo.</div>
        )}
      </div>

      {/* Modal for adding product */}
      {modalProduct && (
        <div className="image-modal" onClick={closeProductModal} role="dialog" aria-modal="true">
          <div className="image-modal-content modal-form" onClick={e => e.stopPropagation()}>
            <button className="modal-close" onClick={closeProductModal} aria-label="Cerrar">×</button>
            <h3>Agregar producto a {category.name}</h3>
            <form onSubmit={submitProduct} className="category-product-form">
              <label>Nombre</label>
              <input value={modalProduct.name} onChange={e => setModalProduct(prev => ({ ...prev, name: e.target.value }))} />

              <label>Precio</label>
              <input type="number" value={modalProduct.price} onChange={e => setModalProduct(prev => ({ ...prev, price: e.target.value }))} />

              <label>Descripción</label>
              <textarea value={modalProduct.description} onChange={e => setModalProduct(prev => ({ ...prev, description: e.target.value }))} />

              <label>Código</label>
              <input value={modalProduct.code} onChange={e => setModalProduct(prev => ({ ...prev, code: e.target.value }))} />

              <label>Stock</label>
              <input type="number" value={modalProduct.stock} onChange={e => setModalProduct(prev => ({ ...prev, stock: e.target.value }))} />

              <label>Imágenes</label>
              <input type="file" accept="image/*" multiple onChange={e => handleFileChange(e.target.files)} />
              <div className="preview-list">
                {modalProduct.previews && modalProduct.previews.map((src, i) => (
                  <div className="preview-item" key={i}>
                    <img src={src} alt={`preview-${i}`} />
                  </div>
                ))}
              </div>

              <div className="form-actions">
                <button className="btn-primary" type="submit">{editingProduct ? 'Actualizar' : 'Agregar'}</button>
                <button type="button" onClick={closeProductModal}>Cancelar</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Image Modal */}
      {modalSrc && (
        <div className="image-modal" onClick={closeImageModal} role="dialog" aria-modal="true">
          <div className="image-modal-content" onClick={e => e.stopPropagation()}>
            <button className="modal-close" onClick={closeImageModal} aria-label="Cerrar">×</button>
            <div style={{display:'flex',gap:8,alignItems:'center',marginBottom:8}}>
              <button
                type="button"
                className="btn-primary"
                onClick={() => window.open(modalSrc, '_blank')}
              >
                Abrir en pestaña
              </button>
            </div>
            <img src={modalSrc} alt="Imagen en tamaño original" className="modal-image" />
          </div>
        </div>
      )}
    </div>
  );
}
