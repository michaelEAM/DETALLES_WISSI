import React, { useEffect, useState, memo } from 'react';
import './product-styles.css';

const ProductList = memo(function ProductList({ products = [], categories = [], onDelete }) {
  const [modalSrc, setModalSrc] = useState(null);
  const [query, setQuery] = useState('');

  useEffect(() => {
    function onKey(e) {
      if (e.key === 'Escape') setModalSrc(null);
    }
    window.addEventListener('keydown', onKey);
    return () => window.removeEventListener('keydown', onKey);
  }, []);

  function openModal(src) {
    setModalSrc(src);
  }

  function closeModal() {
    setModalSrc(null);
  }

  if (!products || products.length === 0) {
    return <div className="empty">No hay productos. Agrega uno nuevo.</div>;
  }

  const q = (query || '').trim().toLowerCase();
  const filtered = q
    ? products.filter(p => {
        const searchableText = `${p.name} ${p.description} ${p.code} ${p.price} ${p.stock}`.toLowerCase();
        return searchableText.includes(q);
      })
    : products;

  return (
    <>
      <div className="product-list-header">
        <div className="search-bar">
          <input
            className="search-input"
            value={query}
            onChange={e => setQuery(e.target.value)}
            placeholder="Buscar por nombre, descripción, precio, id..."
          />
          {query && <button className="search-clear" onClick={() => setQuery('')}>Limpiar</button>}
        </div>
        <div className="search-count">Mostrando {filtered.length} de {products.length}</div>
      </div>

      <div className="product-grid">
        {filtered.map(product => (
          <article className="product-card" key={product.id}>
            <div className="product-media">
              {product.images && product.images.length > 0 ? (
                <img
                  src={product.images[0]}
                  alt={product.name}
                  className="product-main-img clickable"
                  onClick={() => openModal(product.images[0])}
                />
              ) : (
                <div className="product-placeholder">Sin imagen</div>
              )}
            </div>

            <div className="product-body">
              <h3 className="product-title">{product.name}</h3>
              <p className="product-desc">{product.description}</p>
              <div className="product-meta">
                <span><strong>Código:</strong> {product.code}</span>
                <span><strong>Precio:</strong> ${product.price}</span>
                <span><strong>Stock:</strong> {product.stock}</span>
                <span><strong>Categoría:</strong> {categories.find(c => c.id === product.category)?.name || product.category}</span>
              </div>
              <button className="btn-delete" onClick={() => onDelete && onDelete(product.id)}>Eliminar</button>

              {product.images && product.images.length > 1 && (
                <div className="thumbs">
                  {product.images.map((src, i) => (
                    <img
                      key={i}
                      src={src}
                      alt={`${product.name} ${i}`}
                      className="thumb clickable"
                      onClick={() => openModal(src)}
                    />
                  ))}
                </div>
              )}
            </div>
          </article>
        ))}
      </div>

      {modalSrc && (
        <div className="image-modal" onClick={closeModal} role="dialog" aria-modal="true">
          <div className="image-modal-content" onClick={e => e.stopPropagation()}>
            <button className="modal-close" onClick={closeModal} aria-label="Cerrar">×</button>
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
    </>
  );
});

export default ProductList;
