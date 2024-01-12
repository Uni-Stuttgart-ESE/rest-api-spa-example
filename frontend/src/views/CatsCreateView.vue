<script setup lang="ts">
import { Button, Input } from "agnostic-vue";
import config from "@/config";
import { showToast, Toast } from "@/ts/toasts";
import { faCheck, faXmark } from "@fortawesome/free-solid-svg-icons";
import { useRouter } from 'vue-router';
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

const router = useRouter();

let cat = {
    name: "",
    ageInYears: 0,
    picUrl: ""
}

function postCat() {
    // executed on form submit
    fetch(`${config.apiBaseUrl}/cats`, {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(cat)
    })
        .then(response => response.json())
        .then(data => {
            showToast(new Toast("Alert", `Successfully created a new cat with ID ${data.id}!`, "success", faCheck, 5));
            router.push('/cats');
        })
        .catch(error => showToast(new Toast("Error", error, "error", faXmark, 10)));
}

</script>

<template>
    <div class="mainContent">
        <h1>Create New Cat</h1>
        <div>
            <form @submit.prevent="postCat()">
                <Input id="name" v-model="cat.name" placeholder="Enter name…" label="Name" type="text" required />
                <Input id="age" v-model="cat.ageInYears" placeholder="Enter age…" label="Age" type="number" min="0" max="99"
                    required />
                <Input id="url" v-model="cat.picUrl" placeholder="Enter URL…" label="Picture URL" type="url" required />
                <div class="mbs16">
                    <ButtonGroup ariaLabel="FormButtons">
                        <Button isGrouped mode="primary" type="submit">
                            <FontAwesomeIcon class="mie4" icon="arrow-up-from-bracket" />
                            Submit
                        </Button>
                        <Button isGrouped @click="router.push('/cats');" mode="natural">
                            <FontAwesomeIcon class="mie4" icon="xmark" />
                            Cancel
                        </Button>
                    </ButtonGroup>
                </div>
            </form>
        </div>
    </div>
</template>

<style scoped>
.mainContent {
    width: clamp(25ch, 100%, 50ch);
}
</style>