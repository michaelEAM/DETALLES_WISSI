import { ref, push, get, update, remove, onValue } from 'firebase/database';
import { database } from '../firebase';

class FirebaseService {
  constructor() {
    this.db = database;
  }

  // Categorías
  async getCategories() {
    const categoriesRef = ref(this.db, 'categorias');
    const snapshot = await get(categoriesRef);
    const data = snapshot.val();
    return data ? Object.keys(data).map(key => ({ id: key, ...data[key] })) : [];
  }

  async addCategory(category) {
    const categoriesRef = ref(this.db, 'categorias');
    const result = await push(categoriesRef, {
      nombre: category.name,
      descripcion: category.description
    });
    return result.key;
  }

  async updateCategory(id, category) {
    const categoryRef = ref(this.db, `categorias/${id}`);
    await update(categoryRef, {
      nombre: category.name,
      descripcion: category.description
    });
  }

  async deleteCategory(id) {
    const categoryRef = ref(this.db, `categorias/${id}`);
    await remove(categoryRef);
  }

  // Productos
  async getProducts() {
    const productsRef = ref(this.db, 'productos');
    const snapshot = await get(productsRef);
    const data = snapshot.val();
    return data ? Object.keys(data).map(key => ({ id: key, ...data[key] })) : [];
  }

  async addProduct(product) {
    const productsRef = ref(this.db, 'productos');
    const result = await push(productsRef, product);
    return result.key;
  }

  async updateProduct(id, product) {
    const productRef = ref(this.db, `productos/${id}`);
    await update(productRef, product);
  }

  async deleteProduct(id) {
    const productRef = ref(this.db, `productos/${id}`);
    await remove(productRef);
  }

  // Real-time listeners
  onCategoriesChange(callback) {
    const categoriesRef = ref(this.db, 'categorias');
    return onValue(categoriesRef, (snapshot) => {
      const data = snapshot.val();
      const categories = data ? Object.keys(data).map(key => ({ id: key, ...data[key] })) : [];
      callback(categories);
    });
  }

  onProductsChange(callback) {
    const productsRef = ref(this.db, 'productos');
    return onValue(productsRef, (snapshot) => {
      const data = snapshot.val();
      const products = data ? Object.keys(data).map(key => ({ id: key, ...data[key] })) : [];
      callback(products);
    });
  }
}

export default new FirebaseService();
