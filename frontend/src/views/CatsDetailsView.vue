<script setup lang="ts">
import { Button, Input, Spinner, ButtonGroup } from "agnostic-vue";
import config from "@/config";
import { showToast, Toast } from "@/ts/toasts";
import { faCheck, faXmark } from "@fortawesome/free-solid-svg-icons";
import { useRouter, useRoute } from 'vue-router';
import { onMounted, type Ref, ref } from "vue";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

enum PageState {
    LOADING,
    CAT_FOUND,
    NOT_FOUND
}

const router = useRouter();
const route = useRoute();
const catId = ref(route.params.id);
const pageState = ref(PageState.LOADING)

let cat = {
    name: "",
    ageInYears: 0,
    picUrl: ""
}

function putCat() {
    // executed on form submit
    fetch(`${config.apiBaseUrl}/cats/${catId.value}`, {
        method: "PUT",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(cat)
    })
        .then(response => response.json())
        .then(data => {
            showToast(new Toast("Alert", `Successfully updated cat with ID ${data.id}!`, "success", faCheck, 5));
            router.push('/cats');
        })
        .catch(error => showToast(new Toast("Error", error, "error", faXmark, 10)));
}

function deleteCat() {
    // executed on form submit
    fetch(`${config.apiBaseUrl}/cats/${catId.value}`, {
        method: "DELETE"
    })
        .then(response => response.json())
        .then(data => {
            showToast(new Toast("Alert", `Successfully deleted cat with ID ${data.id}!`, "success", faCheck, 5));
            router.push('/cats');
        })
        .catch(error => showToast(new Toast("Error", error, "error", faXmark, 10)));

}

function getCatDetails(id) {
    fetch(`${config.apiBaseUrl}/cats/${id}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Failed to fetch cat with ID ${catId.value}`);
            }
            return response.json();
        })
        .then(data => {
            cat = data;
            pageState.value = PageState.CAT_FOUND;
        })
        .catch(error => {
            showToast(new Toast("Error", error, "error", faXmark, 10));
            pageState.value = PageState.NOT_FOUND;
        });
}

onMounted(() => getCatDetails(catId.value));
</script>

<template>
    <div class="mainContent">
        <h1>Details of cat with ID {{ catId }}</h1>
        <div>
            <div v-if="pageState == PageState.LOADING">
                <Spinner />
                Loading...
            </div>
            <div v-else-if="pageState == PageState.CAT_FOUND">
                <img v-bind:alt="cat.name" v-bind:src="cat.picUrl" class="catLogo" />
                <form @submit.prevent="putCat()">
                    <Input id="name" v-model="cat.name" placeholder="Enter name…" label="Name" type="text" required />
                    <Input id="age" v-model="cat.ageInYears" placeholder="Enter age…" label="Age" type="number" min="0"
                        max="99" required />
                    <Input id="url" v-model="cat.picUrl" placeholder="Enter URL…" label="Picture URL" type="url" required />
                    <div class="mbs16">
                        <ButtonGroup ariaLabel="FormButtons">
                            <Button isGrouped mode="primary" type="submit">
                                <FontAwesomeIcon class="mie4" icon="arrow-up-from-bracket" />
                                Submit Changes
                            </Button>
                            <Button @click="deleteCat()" mode="secondary">
                                <FontAwesomeIcon class="mie4" icon="trash" />
                                Delete
                            </Button>
                            <Button isGrouped @click="router.push('/cats');" mode="natural">
                                <FontAwesomeIcon class="mie4" icon="xmark" />
                                Cancel
                            </Button>
                        </ButtonGroup>
                    </div>
                </form>
            </div>
            <div v-else-if="pageState == PageState.NOT_FOUND">
                Sorry, but this cat is in another castle.
                We could not find any cats with the ID {{ catId }}.
            </div>
            <div v-else>
                This page state should not be reachable. If you still managed to do so, congrats!
            </div>
        </div>
    </div>
</template>

<style scoped>
.catLogo {
    height: 200px;
    border-radius: 50%;
    display: block;
    margin-left: auto;
    margin-right: auto;
}

.notFoundCat {
    height: 200px;
}

.mainContent {
    width: clamp(25ch, 100%, 50ch);
}
</style>