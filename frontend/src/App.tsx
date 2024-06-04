import './App.css';
import { useEffect, useState } from 'react';

// ------------- API -------------------

function service(url: string) {
    async function get<T>(path: string) {
        const response = await fetch(url + path);
        if (!response.ok) throw new Error(`Failed to fetch`);
        const result = await response.json();
        return result as T;
    }

    async function post<T>(path: string, body: T) {
        const response = await fetch(url + path, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(body)
        });
        if (!response.ok) throw new Error(`Failed to fetch`);
    }

    async function del(path: string) {
        const response = await fetch(url + path, {
            method: "DELETE",
        });
        if (!response.ok) throw new Error(`Failed to fetch`);
    }

    return { get, post, del };
}

const categoryService = service("/category");
const productService = service("/product");

interface Product {
    id: number;
    name: string;
    price: number;
    details: string;
    category: number;
}

interface Category {
    id: number;
    name: string;
}

// ----------- UI Components ------------------

function Modal({ title, children, close }: React.PropsWithChildren<{ title: string, close: () => void }>) {
    return <div className="modal">
        <div className="modal-header">
            <div className="modal-title">{title}</div>
            <div className="modal-close" onClick={close}>╳</div>
        </div>
        {children}
    </div>
}

// -------------- Product UI ---------------

function ProductUI({ product, refresh, categories }: { product: Product, refresh: () => void, categories: Category[] }) {
    async function deleteProduct() {
        const confirmed = window.confirm(`Willst du wirklich ${product.name} löschen?`);
        if (!confirmed) return;

        await productService.del(`/delete-product/${product.id}`);

        window.alert("Produkt gelöscht");

        refresh();
    }

    return <div className="product">
        <div className="product-header">
            <div className="product-name">{product.name}</div>
            <div className="product-category">
                <CategoryUI refresh={refresh} categoryId={product.category} categories={categories} />
            </div>
            <div className="spacer" />
            <div className="product-price">{product.price} $</div>
        </div>
        <div className="product-description">
            {product.details}
        </div>

        <div className="product-actions">
            <button onClick={deleteProduct}>Löschen</button>
        </div>
    </div>
}

function AddProduct({ refresh, categories }: { refresh: () => void, categories: Category[] }) {
    const [name, setName] = useState<string>("");
    const [details, setDetails] = useState<string>("");
    const [price, setPrice] = useState<string>("");
    const [categoryId, setCategoryId] = useState<number>(0);

    async function addProduct() {
        await productService.post<Omit<Product, "id">>("/add-product", {
            name,
            details,
            price: parseFloat(price),
            category: categoryId
        });

        window.alert("Product erfolgreich angelegt");
        refresh();
    }

    const invalidPrice = (!!price && isNaN(parseFloat(price)));
    const valid = name.length > 0 && details.length > 0 && price.length > 0 && !invalidPrice && categoryId !== 0;
    return <div className="column">
        <div className="row">
            <label>Name</label>
            <input value={name} onChange={e => setName(e.target.value)} />
        </div>
        <div className="row">
            <label>Beschreibung</label>
            <input value={details} onChange={e => setDetails(e.target.value)} />
        </div>
        <div className="row">
            <label>Preis</label>
            <input value={price} onChange={e => setPrice(e.target.value)} />
            {invalidPrice && <i>Invalider Preis</i>}
        </div>

        <select onChange={e => setCategoryId(categories[e.target.selectedIndex - 1]?.id ?? 0)}>
            <option>Wähle eine Kategorie</option>
            {categories.map(it => <option selected={it.id === categoryId}>{it.name}</option>)}
        </select>

        <div className="row">
            <button disabled={!valid} onClick={addProduct}>Hinzufügen</button>
        </div>
    </div>;
}

// -------------- Category UI ---------------

function CategoryUI({ categoryId, categories, refresh }: { categoryId: number, categories: Category[], refresh: () => void }) {
    const category = categories.find(it => it.id === categoryId);

    async function deleteCategory() {
        await categoryService.del("/delete-category/" + categoryId);

        window.alert("Kategorie gelöscht");

        refresh();
    }

    return <div className="category">
        {category?.name ?? "?"}
        <button className="category-delete" onClick={deleteCategory}>🗑</button>
    </div>
}

function AddCategory({ refresh }: { refresh: () => void }) {
    const [name, setName] = useState<string>("");

    async function addCategory() {
        await categoryService.post<Omit<Category, "id">>("/add-category", {
            name
        });

        window.alert("Kategorie erfolgreich angelegt");
        refresh();
    }

    return <>
        <label>Name</label>
        <input value={name} onChange={e => setName(e.target.value)} />
        <button disabled={!name.length} onClick={addCategory}>Hinzufügen</button>
    </>;
}

// -------------- App ---------------

function App() {
  const [addProduct, setAddProduct] = useState(false);
  const [addCategory, setAddCategory] = useState(false);
  
  const [products, setProducts] = useState<Product[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);

  const [searchDetails, setSearchDetails] = useState("");
  const [searchMinPrice, setSearchMinPrice] = useState("");
  const [searchMaxPrice, setSearchMaxPrice] = useState("");


  async function refresh() {
    setProducts(await productService.get<Product[]>("/get-products"));
    setCategories(await categoryService.get<Category[]>("/get-categories"));
  }

  async function search() {
    const minPrice = parseFloat(searchMinPrice) || 0;
    const maxPrice = parseFloat(searchMaxPrice) || 1_000_000;

    const searchedProducts = await productService.get<Product[]>(`/get-product-by-search/${searchDetails}/${minPrice}/${maxPrice}`);
    setProducts(searchedProducts);
  }

  useEffect(() => { refresh(); }, []);

  return (
    <div className="App">
        <h1>Webshop</h1>
        <h2>Produkte</h2>

        <div className="search">
            <input placeholder="Suche" value={searchDetails} onChange={e => setSearchDetails(e.target.value)}/>
            <input type="number" placeholder="Mindestpreis" value={searchMinPrice} onChange={e => setSearchMinPrice(e.target.value)} />
            <input type="number" placeholder="Maximalpreis" value={searchMaxPrice} onChange={e => setSearchMaxPrice(e.target.value)} />
            <button onClick={search}>Suchen</button>
        </div>
        
        <div className="products">
            {products.map(product => <ProductUI categories={categories} refresh={refresh} product={product} />)}
        </div>
        <div className="actions">
            <button onClick={() => setAddProduct(true)}>Produkt anlegen</button>
            <button onClick={() => setAddCategory(true)}>Kategorie anlegen</button>
        </div>
        {addProduct && <Modal title="Produkt hinzufügen" close={() => setAddProduct(false)}>
            <AddProduct categories={categories} refresh={() => { setAddProduct(false); refresh(); }} />
        </Modal>}
        {addCategory && <Modal title="Kategorie hinzufügen" close={() => setAddCategory(false)}>
            <AddCategory refresh={() => { setAddCategory(false); refresh(); }} />
        </Modal>}
    </div>
  );
}

export default App;
