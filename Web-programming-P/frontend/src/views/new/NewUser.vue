<template>
    <div>
        <b-card style="max-width: 30rem;" class="ml-auto mr-auto mt-3">
            <b-card-title align="center">New User</b-card-title>
            <b-img center src="//ssl.gstatic.com/accounts/ui/avatar_1x.png" class="profile-img-card rounded-circle"></b-img>

            <br>

            <b-form @submit.prevent="handleSubmitAddUser">
                <b-form-group id="input-group-username" label="Username:" label-for="input-username">
                    <b-form-input id="input-username" v-model="user.username" :state="usernameValidation"
                                  class="form-control" type="text" required placeholder="Enter username"></b-form-input>
                    <b-form-invalid-feedback :state="usernameValidation">
                        Username must be 2-20 characters long and can contain only letters.
                    </b-form-invalid-feedback>
                </b-form-group>

                <b-form-group id="input-group-password1" label="Password:" label-for="input-password1">
                    <b-form-input id="input-password1" v-model="user.password1" :state="password1Validation"
                                  class="form-control" type="password" required placeholder="Enter password"></b-form-input>
                    <b-form-invalid-feedback :state="password1Validation">
                        Password must be 6-20 characters long and must contain both numbers and letters.
                    </b-form-invalid-feedback>
                </b-form-group>

                <b-form-group id="input-group-password2" label="Confirm password:" label-for="input-password2">
                    <b-form-input id="input-password2" v-model="user.password2" :state="password2Validation"
                                  type="password" required placeholder="Confirm password"></b-form-input>
                </b-form-group>

                <b-form-group id="input-group-type" label="Type:" label-for="input-user-type">
                    <b-form-select id="input-user-type" v-model="user.type" :state="typeValidation" :options="types"
                                   required></b-form-select>
                </b-form-group>

                <div class="text-center">
                    <b-button type="submit" variant="primary" class="align-self-center">Submit</b-button>
                </div>
            </b-form>

            <br v-if="showDismissibleAlert">

            <b-alert v-model="showDismissibleAlert" :variant="successful ? 'success' : 'danger'" dismissible>
                {{message}}
            </b-alert>

        </b-card>
    </div>
</template>

<script>
    import User from "@/models/user";

    export default {
        name: "NewUser",
        data() {
            return {
                user: new User(null, null, null, null),
                successful: false,
                message: '',
                types: [{text: 'Select Type', value: null}, 'CUSTOMER', 'ADMIN'],
                showDismissibleAlert: false
            }
        },
        computed: {
            usernameValidation() {
                let letters = /^[a-zA-Z]+$/
                if (!this.user.username) return null
                if (this.user.username.length > 1 && this.user.username.length < 21 && this.user.username.match(letters)) return true
                return false
            },
            password1Validation() {
                let numbersANDletters = /^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]+)$/
                if (!this.user.password1) return null
                if (this.user.password1.length > 5 && this.user.password1.length < 21 && this.user.password1.match(numbersANDletters)) return true
                return false
            },
            password2Validation() {
                if (!this.user.password2) return null
                if (this.password1Validation && this.user.password1 === this.user.password2) return true
                return false
            },
            typeValidation() {
                if (this.user.type) return true
                return null
            }
        },
        methods: {
            handleSubmitAddUser() {
                this.message = '';
                if (this.usernameValidation && this.password1Validation && this.password2Validation && this.typeValidation) {
                    this.$store.dispatch('auth/register', this.user).then(
                        data => {
                            this.message = data.message;
                            this.successful = true;
                            this.$router.push('/');
                        },
                        error => {
                            this.message = error.response.data.message
                            this.showDismissibleAlert = true
                            this.successful = false;
                        }
                    );
                }
            },
            currentUser() {
                return this.$store.state.auth.user;
            },
            isAdminLoggedIn() {
                if (this.currentUser() && this.currentUser().type) {
                    if (this.currentUser().type === 'ADMIN') {
                        return true;
                    }
                }
                return false;
            }
        },
        mounted() {
            if (!this.isAdminLoggedIn()) {
                this.$router.push('/');
            }
        }
    }
</script>

<style scoped>

</style>