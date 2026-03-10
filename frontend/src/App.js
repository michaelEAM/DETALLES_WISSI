import React, { useEffect, useState, useCallback } from 'react';
import './App.css';
import { BrowserRouter as Router, Routes, Route, NavLink } from 'react-router-dom';
import ProductList from './components/ProductList';
import ProductForm from './components/ProductForm';
import CategoryDashboard from './components/CategoryDashboard';
import CategoryDetail from './components/CategoryDetail';

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080';
console.log("API_BASE_URL:", API_BASE_URL);

function App() {
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Fetch categories and products from API on mount
  const fetchData = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const [catsRes, prodsRes] = await Promise.all([
        fetch(`${API_BASE_URL}/api/categorias`),
        fetch(`${API_BASE_URL}/api/productos`)
      ]);

      if (!catsRes.ok || !prodsRes.ok) {
        throw new Error('Error al cargar datos del servidor');
      }

      const catsData = await catsRes.json();
      const prodsData = await prodsRes.json();

      // Map backend to frontend
      const categoriesMapped = catsData.map(cat => ({
        id: cat.id_categoria || cat.id,
        name: cat.nombre,
        description: cat.descripcion,
        products: []
      }));
      const productsMapped = prodsData.map(prod => ({
        id: prod.idProducto,
        code: prod.codigo,
        name: prod.nombre,
        description: prod.descripcion,
        price: prod.precio,
        imageUrl: prod.imagenUrl ? `${API_BASE_URL}${prod.imagenUrl}` : '',
        stock: prod.stock,
        category: prod.id_categoria || prod.categoriaId,
        images: prod.imagenUrl ? [`${API_BASE_URL}${prod.imagenUrl}`] : []
      }));

      // Populate categories.products
      categoriesMapped.forEach(cat => {
        cat.products = productsMapped.filter(p => p.category === cat.id);
      });

      setCategories(categoriesMapped);
      setProducts(productsMapped);
    } catch (error) {
      console.error('Error fetching data:', error);
      setError(error.message);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  async function handleAdd(formData) {
    try {
      const response = await fetch(`${API_BASE_URL}/api/productos`, {
        method: 'POST',
        body: formData
      });
      if (response.ok) {
        fetchData();
      } else {
        alert('Error adding product');
      }
    } catch (error) {
      console.error('Error:', error);
    }
  }

  async function handleUpdate(id, formData) {
    try {
      const response = await fetch(`${API_BASE_URL}/api/productos/${id}`, {
        method: 'PUT',
        body: formData
      });
      if (response.ok) {
        fetchData();
      } else {
        alert('Error updating product');
      }
    } catch (error) {
      console.error('Error:', error);
    }
  }

  async function handleDelete(id) {
    try {
      const response = await fetch(`${API_BASE_URL}/api/productos/${id}`, { method: 'DELETE' });
      if (response.ok) {
        fetchData();
      } else {
        alert('Error deleting product');
      }
    } catch (error) {
      console.error('Error:', error);
    }
  }

  const handleAddCategory = useCallback(async (category) => {
    try {
      const response = await fetch(`${API_BASE_URL}/api/categorias`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ nombre: category.name, descripcion: category.description })
      });
      if (response.ok) {
        const newCategory = await response.json();
        const categoryMapped = {
          id: newCategory.id,
          name: newCategory.nombre,
          description: newCategory.descripcion,
          products: []
        };
        setCategories(prev => [...prev, categoryMapped]);
        return newCategory.id;
      } else {
        throw new Error('Error al agregar categoría');
      }
    } catch (error) {
      console.error('Error:', error);
      alert(error.message);
      return null;
    }
  }, []);

  const handleDeleteCategory = useCallback(async (id) => {
    try {
      const response = await fetch(`${API_BASE_URL}/api/categorias/${id}`, { method: 'DELETE' });
      if (response.ok) {
        setCategories(prev => prev.filter(cat => cat.id !== id));
      } else {
        throw new Error('Error al eliminar categoría');
      }
    } catch (error) {
      console.error('Error:', error);
      alert(error.message);
    }
  }, []);

  if (loading) {
    return (
      <div className="App app-container">
        <div style={{ padding: 20, textAlign: 'center' }}>Cargando...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="App app-container">
        <div style={{ padding: 20, textAlign: 'center', color: 'red' }}>
          Error: {error}
          <br />
          <button onClick={fetchData}>Reintentar</button>
        </div>
      </div>
    );
  }

  return (
    <Router>
      <div className="App app-container">
        <header className="app-header">
          <h1>DETALLES WISSI</h1>
          <nav className="app-nav">
              <NavLink to="/" className={({isActive}) => isActive ? 'active' : ''} end>Ver productos</NavLink>
              <NavLink to="/dashboard" className={({isActive}) => isActive ? 'active' : ''}>Dashboard</NavLink>
          </nav>
        </header>

        <main className="app-main">
          <Routes>
            <Route path="/" element={<ProductList products={products} categories={categories} onDelete={handleDelete} />} />
            <Route path="/dashboard" element={<CategoryDashboard categories={categories} onAddCategory={handleAddCategory} onDeleteCategory={handleDeleteCategory} />} />
            <Route path="/category/:id" element={<CategoryDetail categories={categories} onAddProduct={handleAdd} onUpdateProduct={handleUpdate} onDeleteProduct={handleDelete} onDeleteCategory={handleDeleteCategory} />} />
            <Route path="/categorias/:idCategoria/productos/nuevo" element={<ProductForm onAdd={handleAdd} categories={categories} />} />
            <Route path="*" element={<div style={{padding:20}}>Ruta no encontrada</div>} />
          </Routes>
        </main>

        <footer className="app-footer">Detalles</footer>
      </div>
    </Router>
  );
}

export default App;
