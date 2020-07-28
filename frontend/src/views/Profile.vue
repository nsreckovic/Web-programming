<template>
    <b-card style="max-width: 60%;" class="ml-auto mr-auto mt-3">
        <header class="jumbotron">
            <h3>
                <strong>{{currentUser.username}}'s</strong> Profile
            </h3>
        </header>
        <p>
            <strong>Type:</strong>
            {{currentUser.type}}
        </p>
        <p>
            <strong>Token:</strong>
            {{currentUser.jwt}}
        </p>
        <p>
            <strong>ID:</strong>
            {{currentUser.id}}
        </p>
    </b-card>
</template>

<script>
    export default {
        name: 'Profile',
        data() {
            return {
                // Notification
                successful: false,
                variant: 'warning',
                message: '',
                showDismissibleAlert: false,
                success_message: '',
            }
        },
        computed: {
            currentUser() {
                return this.$store.state.auth.user;
            },
            isAdminLoggedIn() {
                if (this.currentUser && this.currentUser.type) {
                    if (this.currentUser.type === 'ADMIN') {
                        return true;
                    }
                }
                return false;
            }
        },
        mounted() {
            if (!this.currentUser) {
                this.$router.push('/login');
            }

        }
    };
</script>