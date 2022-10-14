import Vue from "vue";
import VueRouter from "vue-router";
import Home from "../views/Home.vue";
import About from "../views/About.vue";
import Cats from "../views/Cats.vue";
import CatCreate from "../views/Cat-create.vue";
import CatDetails from "../views/Cat-details.vue";

Vue.use(VueRouter);

// all frontend routes of the app, i.e. bind a path to a Vue component
const routes = [
    {
        path: "/",
        name: "Home",
        component: Home
    },
    {
        path: "/about",
        name: "About",
        component: About
    },
    {
        path: "/cats",
        name: "Cats",
        component: Cats
    },
    {
        path: "/create-cat",
        name: "CatCreate",
        component: CatCreate
    },
    {
        path: "/cats/:id",
        name: "CatDetails",
        component: CatDetails
    }
];

const router = new VueRouter({
    routes
});

export default router;
