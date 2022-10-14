<template>
    <div>
        <h1>Create New Cat</h1>
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
    name: "CatCreate",
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
                .post(`${config.apiBaseUrl}/cats`, this.cat)
                .then((response) => {
                    // log response data to the browser console
                    console.log(response.data);
                    // show success message
                    showToastMessage(
                        this,
                        "Alert",
                        `Successfully created a new cat with ID ${response.data.id}!`,
                        "success"
                    );
                    // navigate to cat list view
                    this.$router.push(`/cats`);
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
        }
    }
};
</script>

<style></style>
