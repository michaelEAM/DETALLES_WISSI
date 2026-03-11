import React, { useEffect, useState, useCallback } from 'react';
import './App.css';
import { HashRouter as Router, Routes, Route, NavLink } from 'react-router-dom';
import ProductList from './components/ProductList';
import ProductForm from './components/ProductForm';
import CategoryDashboard from './components/CategoryDashboard';
import CategoryDetail from './components/CategoryDetail';
import FirebaseService from './services/FirebaseService';

function App() {
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Fetch categories and products from Firebase on mount
  const fetchData = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      
      const [catsData, prodsData] = await Promise.all([
        FirebaseService.getCategories(),
        FirebaseService.getProducts()
      ]);

      // Map Firebase data to frontend format
      const categoriesMapped = catsData.map(cat => ({
        id: cat.id,
        name: cat.nombre,
        description: cat.descripcion,
        products: []
      }));
      
      const productsMapped = prodsData.map(prod => ({
        id: prod.id,
        code: prod.codigo || '',
        name: prod.nombre,
        description: prod.descripcion || '',
        price: prod.precio || 0,
        imageUrl: prod.imagenUrl || '',
        stock: prod.stock || 0,
        category: prod.id_categoria || prod.categoriaId,
        images: prod.imagenUrl ? [prod.imagenUrl] : []
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
      // Convert FormData to object for Firebase
      const productData = Object.fromEntries(formData.entries());
      await FirebaseService.addProduct(productData);
      fetchData();
    } catch (error) {
      console.error('Error:', error);
      alert('Error adding product');
    }
  }

  async function handleUpdate(id, formData) {
    try {
      // Convert FormData to object for Firebase
      const productData = Object.fromEntries(formData.entries());
      await FirebaseService.updateProduct(id, productData);
      fetchData();
    } catch (error) {
      console.error('Error:', error);
      alert('Error updating product');
    }
  }

  async function handleDelete(id) {
    try {
      await FirebaseService.deleteProduct(id);
      fetchData();
    } catch (error) {
      console.error('Error:', error);
      alert('Error deleting product');
    }
  }

  const handleAddCategory = useCallback(async (category) => {
    try {
      const newId = await FirebaseService.addCategory(category);
      const categoryMapped = {
        id: newId,
        name: category.name,
        description: category.description,
        products: []
      };
      setCategories(prev => [...prev, categoryMapped]);
      return newId;
    } catch (error) {
      console.error('Error:', error);
      alert(error.message);
      return null;
    }
  }, []);

  const handleDeleteCategory = useCallback(async (id) => {
    try {
      await FirebaseService.deleteCategory(id);
      setCategories(prev => prev.filter(cat => cat.id !== id));
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
