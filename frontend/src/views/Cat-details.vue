<template>
    <div>
        <h1>Cat Details: {{ cat.name }}</h1>
        <div>
            <b-form @submit="onSubmit">
                <b-form-group label="Name:">
                    <b-form-input
                        v-model="cat.name"
                        type="text"
                        required
                    ></b-form-input>
                </b-form-group>
                <b-form-group label="Age (years):">
                    <b-form-input
                        v-model="cat.ageInYears"
                        type="number"
                        required
                    ></b-form-input>
                </b-form-group>
                <b-form-group label="Picture URL:">
                    <b-form-input
                        v-model="cat.picUrl"
                        type="text"
                    ></b-form-input>
                </b-form-group>

                <b-button type="submit" variant="primary">Submit</b-button>
                <b-button
                    type="button"
                    variant="danger"
                    v-on:click="deleteCat()"
                    >Delete</b-button
                >
            </b-form>
        </div>
    </div>
</template>

<script>
// import configuration with API url; @ refers to the src directory
import config from "@/config";
// import library for HTTP requests
import axios from "axios";
// import JS method to use in this view
import { showToastMessage } from "../js/util";

export default {
    name: "CatDetails",
    data() {
        return {
            cat: {
                name: "",
                ageInYears: 0,
                picUrl: ""
            }
        };
    },
    methods: {
        // executed on form submit
        onSubmit: function (event) {
            event.preventDefault();
            axios
                .put(`${config.apiBaseUrl}/cats/${this.cat.id}`, this.cat)
                .then((response) => {
                    // log response data to the browser console
                    console.log(response.data);
                    // show success message
                    showToastMessage(
                        this,
                        "Alert",
                        `Successfully updated cat with ID ${response.data.id}!`,
                        "success"
                    );
                })
                .catch((error) => {
                    // log error
                    console.log(error);
                    // show error message
                    const errObject = error.response.data.errors[0];
                    showToastMessage(
                        this,
                        "Alert",
                        `Error: ${errObject.field} ${errObject.defaultMessage}!`,
                        "danger"
                    );
                });
        },
        deleteCat: function () {
            // send DELETE request to API to delete the cat by ID
            axios
                .delete(`${config.apiBaseUrl}/cats/${this.cat.id}`)
                .then(() => {
                    // show success message
                    showToastMessage(
                        this,
                        "Alert",
                        `Successfully deleted cat with ID ${this.cat.id}!`,
                        "success"
                    );
                    // navigate to cats list
                    this.$router.push("/cats");
                });
        }
    },

    // executed after the component has been started
    created: function () {
        axios
            .get(`${config.apiBaseUrl}/cats/${this.$route.params.id}`)
            .then((response) => {
                // log response data to the browser console
                console.log(response.data);
                // assign response to data variable
                this.cat = response.data;
            });
    }
};
</script>

<style>
form .btn {
    margin-right: 5px;
}
</style>
