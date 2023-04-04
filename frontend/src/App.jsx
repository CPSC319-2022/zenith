import { BrowserRouter, Route, Routes } from "react-router-dom";
import 'bootstrap/dist/css/bootstrap.min.css';
import './styles/App.css';

// imported components
import Register from "./pages/Register";
import Login from "./pages/Login";
import Logout from "./pages/Logout";
import Home from "./pages/Home";
import SearchResults from "./pages/SearchResults";
import Create from "./pages/Create";
import SinglePost from "./pages/SinglePost";
import Header from "./common-components/Header";
import Footer from "./common-components/Footer";
import Profile from "./pages/Profile";
import AdminWrapper from "./pages/AdminWrapper";

// Layout function needed to apply the Nav and Footer to different child pages
const Layout = ({ children }) => {
    return (
        // React fragment needed because multiple components cannot be used without parent
        <>
            <Header />
            {children}
            <Footer />
        </>
    );
};

// this will render the app
function App() {
    return (
        <div className="app">
            <div className="app-container">
                <BrowserRouter>
                    <Layout>
                        <Routes>
                            <Route path="/" element={<Home />} />
                            <Route path="/searchResults" element={<SearchResults />} />
                            <Route path="/single-post/:id" element={<SinglePost />} />
                            <Route path="/create" element={<Create />} />
                            <Route path="/profile/:id" element={<Profile />} />
                            <Route path="/profile" element={<Profile />} />
                            <Route path="/register" element={<Register />} />
                            <Route path="/login" element={<Login />} />
                            <Route path="/logout" element={<Logout />} />
                            {/* <Route path="/admin" element={<Admin />} /> */}
                            <Route path="/admin" element={<AdminWrapper />} />
                        </Routes>
                    </Layout>
                </BrowserRouter>
            </div>
        </div>
    );
}

export default App;