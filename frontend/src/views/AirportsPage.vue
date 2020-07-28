<template>
    <b-card style="max-width: 50%;" class="ml-auto mr-auto mt-3">
        <b-row class="ml-auto mb-3">
            <b-card-title>
                <h1>Airports</h1>
            </b-card-title>
        </b-row>

        <b-alert v-model="showDismissibleAlert" :variant="variant" dismissible class="my-2">
            {{message}}
        </b-alert>

        <b-row class="ml-auto">
            <h4>All airports:</h4>
            <b-table show-empty
                     small
                     stacked="md"
                     :items="table_airports"
                     :fields="airports_fields"
                     striped
                     hover
                     class="text-center">

                <template v-slot:cell(edit)="row">
                    <b-button size="sm" @click="editAirport(row.item)">Edit</b-button>
                </template>

                <template v-slot:cell(delete)="row">
                    <b-button size="sm" variant="danger" @click="deletePrompt(row.item, row.index, $event.target)">
                        Delete
                    </b-button>
                </template>

            </b-table>
        </b-row>

        <b-modal :id="infoModal.id" hide-footer hide-header centered size="lg" :title="infoModal.title"
                 @hide="resetInfoModal">
            <h4 align="center">Are you sure you want to delete this airport?</h4>
            <div class="text-center mt-5">
                <b-button class="mr-1" variant="outline-secondary" @click="hideModal">Cancel</b-button>
                <b-button class="mr-1" variant="danger" @click="deleteAirport">Delete</b-button>
            </div>
        </b-modal>

    </b-card>
</template>

<script>
    import AirportService from "@/services/airport.service";

    export default {
        name: "AirportsPage",
        data() {
            return {
                // Airports
                table_airports: [],
                airports_fields: [
                    {key: 'ID'},
                    {key: 'name'},
                    {key: 'place'},
                    {key: 'Edit'},
                    {key: 'Delete'},
                ],

                // Notification
                successful: false,
                variant: 'warning',
                message: '',
                showDismissibleAlert: false,
                success_message: '',

                infoModal: {
                    id: 'delete-modal',
                    content: ''
                }
            }
        },
        methods: {
            fetchAirports() {
                AirportService.getAllAirports().then(
                    response => {
                        response.data.forEach((airport) => {
                            this.table_airports.push({
                                ID: airport.id,
                                name: airport.name,
                                place: airport.place,
                                edit: null,
                                delete: null
                            })
                        })
                    },
                    error => {
                        this.message = error.response.data.message
                        this.showDismissibleAlert = true
                        this.successful = false;
                    }
                )
            },
            editAirport(data) {
                this.$router.push({name: 'editAirport', params: data})
            },
            resetInfoModal() {
                this.infoModal.content = null
            },
            hideModal() {
                this.$root.$emit('bv::hide::modal', this.infoModal.id)
            },
            deletePrompt(item, index, button) {
                this.infoModal.content = item
                this.$root.$emit('bv::show::modal', this.infoModal.id, button)
            },
            deleteAirport() {
                AirportService.deleteAirport(this.infoModal.content.ID).then(
                    response => {
                        if (response.status == 200) {
                            this.table_airports = []
                            this.fetchAirports()
                            this.hideModal()
                        }
                    }, error => {
                        if (error.response) this.message = error.response.data.message || error.message
                        else this.message = error.toString()
                        this.variant = "danger"
                        this.showDismissibleAlert = true
                        this.successful = false;
                    }
                )
            },
            currentUser() {
                return this.$store.state.auth.user;
            },
        },
        computed: {
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
            if (!this.isAdminLoggedIn) {
                this.$router.push('/login');
            }
            this.fetchAirports()
        }
    }
</script>

<style scoped>

</style>