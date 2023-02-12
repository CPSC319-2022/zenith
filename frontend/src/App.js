import {createBrowserRouter, Outlet, RouterProvider} from "react-router-dom";
import 'bootstrap/dist/css/bootstrap.min.css';
import './styles/App.css';

// imported components
import Register from "./pages/Register";
import Login from "./pages/Login";
import Home from "./pages/Home";
import Create from "./pages/Create";
import SinglePost from "./pages/SinglePost";
import Header from "./common components/Header";
import Footer from "./common components/Footer";
import Profile from "./pages/Profile";

// comment 

// Layout function needed to apply the Nav and Footer to different child pages
const Layout = () => {
    return (
        // React fragment needed because multiple components cannot be used without parent
        <>
            <Header />
            <Outlet />
            <Footer />
        </>
    );
};


// this will navigate between the different pages of the app
const router = createBrowserRouter([
    {
        path: "/",
        element: <Layout />,
        children: [
            {
                path: "/",
                element: <Home />
            },
            {
                path: "/single-post/:id",
                element: <SinglePost />
            },
            {
                path: "/create",
                element: <Create />
            },
            {
                path: "/profile",
                element: <Profile />
            }
        ]
    },
    {
        path: "/register",
        element: <Register />,
    },
    {
        path: "/login",
        element: <Login />,
    }
]);



// this will render the app
function App() {
    return (
        <div className="app">
            <div className="app-container">
                <RouterProvider router={router} />
            </div>
        </div>
    );

}

export default App;
