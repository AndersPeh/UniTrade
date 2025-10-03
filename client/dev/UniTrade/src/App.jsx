import { useState } from 'react'
import {Route, RouterProvider, createBrowserRouter, createRoutesFromElements} from 'react-router-dom'
import RootLayout from "./component/layout/RootLayout.jsx";
import Home from "./component/home/Home.jsx";
import Products from "./component/product/Products.jsx";
import ProductDetails from "./component/product/ProductDetails";

// Overall structure of main page
// Uses React Router for routing
// RootLayout is the main layout component that contains the header, footer, and outlet for nested
// Home is the main landing page
// Products is the product listing page that contains the search bar and sidebar for filtering products
// With dynamic routing for product names
function App() {
  const router = createBrowserRouter(
      createRoutesFromElements(
          <Route path="/" element={<RootLayout/>}>
              <Route index element={<Home />} />
              <Route path="/products" element={<Products />} />
              <Route path='/products/:name' element={<Products />} />
              <Route
                  path='/products/category/:categoryId/products/'
                  element={<Products />}
              />
              <Route
                  path='/product/:productId/details'
                  element={<ProductDetails />}
              />
          </Route>
      )
  );

  return (
    <RouterProvider router={router}/>
  )
}

export default App
