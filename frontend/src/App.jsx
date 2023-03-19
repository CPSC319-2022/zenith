import { BrowserRouter, Route, Routes } from "react-router-dom";
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
import WIP from "./pages/WIP";

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
                            <Route path="/single-post/:id" element={<SinglePost />} />
                            <Route path="/create" element={<Create />} />
                            {/* <Route path="/profile" element={<Profile />} /> */}
                            <Route path="/wip" element={<WIP />} />
                            <Route path="/register" element={<Register />} />
                            <Route path="/login" element={<Login />} />
                        </Routes>
                    </Layout>
                </BrowserRouter>
            </div>
        </div>
    );
}

export default App;
