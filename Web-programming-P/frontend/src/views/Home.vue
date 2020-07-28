<template>
    <div>
        <header class="jumbotron">
            <h1 align="center">WebAir</h1>
            <h5 align="center">Ultimate airplane ticket online tool.</h5>
        </header>
        <Reservations></Reservations>
    </div>
</template>

<script>
    import Reservations from "@/components/Reservations";

    export default {
        name: 'Home',
        components: {
            Reservations
        },
        data() {
            return {
                content: ''
            };
        },
        mounted() {

        }
    };
</script>