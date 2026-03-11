class BackendService {
  constructor() {
    this.baseURL = 'https://detalles-wissi.onrender.com';
  }

  // Categorías
  async getCategories() {
    const response = await fetch(`${this.baseURL}/api/categorias`);
    if (!response.ok) throw new Error('Error al obtener categorías');
    return await response.json();
  }

  async addCategory(category) {
    const response = await fetch(`${this.baseURL}/api/categorias`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ nombre: category.name, descripcion: category.description })
    });
    if (!response.ok) throw new Error('Error al agregar categoría');
    return await response.text();
  }

  async updateCategory(id, category) {
    const response = await fetch(`${this.baseURL}/api/categorias/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ nombre: category.name, descripcion: category.description })
    });
    if (!response.ok) throw new Error('Error al actualizar categoría');
  }

  async deleteCategory(id) {
    const response = await fetch(`${this.baseURL}/api/categorias/${id}`, {
      method: 'DELETE'
    });
    if (!response.ok) throw new Error('Error al eliminar categoría');
  }

  // Productos
  async getProducts() {
    const response = await fetch(`${this.baseURL}/api/productos`);
    if (!response.ok) throw new Error('Error al obtener productos');
    return await response.json();
  }

  async addProduct(product) {
    const response = await fetch(`${this.baseURL}/api/productos`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(product)
    });
    if (!response.ok) throw new Error('Error al agregar producto');
    return await response.text();
  }

  async updateProduct(id, product) {
    const response = await fetch(`${this.baseURL}/api/productos/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(product)
    });
    if (!response.ok) throw new Error('Error al actualizar producto');
  }

  async deleteProduct(id) {
    const response = await fetch(`${this.baseURL}/api/productos/${id}`, {
      method: 'DELETE'
    });
    if (!response.ok) throw new Error('Error al eliminar producto');
  }
}

const backendService = new BackendService();
export default backendService;
