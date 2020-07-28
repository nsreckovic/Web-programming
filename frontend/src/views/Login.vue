<template>
    <div>
        <b-card style="max-width: 30rem;" class="ml-auto mr-auto mt-3">
            <b-card-title align="center">Log In</b-card-title>
            <b-img center src="//ssl.gstatic.com/accounts/ui/avatar_1x.png"
                   class="profile-img-card rounded-circle"></b-img>
            <br>

            <b-form @submit.prevent="handleLogin">
                <b-form-group id="input-group-username" label="Username:" label-for="input-username">
                    <b-form-input id="input-username" v-model="user.username" :state="usernameValidation"
                                  class="form-control" type="text" required placeholder="Enter username"></b-form-input>
                    <b-form-invalid-feedback :state="usernameValidation">
                        Username must be 2-20 characters long and can contain only letters.
                    </b-form-invalid-feedback>
                </b-form-group>

                <b-form-group id="input-group-password1" label="Password:" label-for="input-password1">
                    <b-form-input id="input-password1" v-model="user.password1" :state="password1Validation"
                                  class="form-control" type="password" required
                                  placeholder="Enter password"></b-form-input>
                    <b-form-invalid-feedback :state="password1Validation">
                        Password must be 6-20 characters long and must contain both numbers and letters.
                    </b-form-invalid-feedback>
                </b-form-group>

                <div class="text-center">
                    <b-button type="submit" variant="primary" class="align-self-center">
                        <span v-show="loading" class="spinner-border spinner-border-sm"></span>
                        <span> Log In</span>
                    </b-button>
                </div>
            </b-form>

            <br v-if="showDismissibleAlert">
            <b-alert v-model="showDismissibleAlert" variant="danger" dismissible>
                {{message}}
            </b-alert>

        </b-card>
    </div>
</template>

<script>
    import User from '../models/user';

    export default {
        name: 'Login',
        data() {
            return {
                user: new User('', ''),
                loading: false,
                message: '',
                showDismissibleAlert: false
            };
        },
        computed: {
            loggedIn() {
                return this.$store.state.auth.status.loggedIn;
            },
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
            }
        },
        created() {
            if (this.loggedIn) {
                this.$router.push('/profile');
            }
        },
        methods: {
            handleLogin() {
                this.loading = true;
                if (!this.usernameValidation || !this.password1Validation) {
                    this.loading = false;
                    return;
                }
                if (this.user.username && this.user.password1) {
                    this.$store.dispatch('auth/login', this.user).then(
                        () => {
                            this.$router.push('/profile');
                        },
                        error => {
                            this.loading = false;
                            if (error.response) this.message = error.response.data.message || error.message
                            else this.message = error.toString()
                            this.showDismissibleAlert = true
                        }
                    );
                }
            }
        }
    };
</script>

<style scoped>
    label {
        display: block;
        margin-top: 10px;
    }

    .profile-img-card {
        width: 96px;
        height: 96px;
        margin: 0 auto 10px;
        display: block;
        -moz-border-radius: 50%;
        -webkit-border-radius: 50%;
        border-radius: 50%;
    }
</style>