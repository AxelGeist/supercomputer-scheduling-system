## Backlog: 

# Must haves:
- User
    - Users must be authorised when trying to make a request
    - Users must be able to make job requests
    - User must be assigned to single/multiple faculties
    - Users must be stored in the database
- Job
    - Users must be able to create new jobs
    - Jobs microservice must generate and store notifications assigned to specific users regarding job updates etc.
    - Users must be able to request their notifications
    - SysAdmins must be able to request the overview of all schedules
    - FacultyAccounts must be able to approve jobs
    - Jobs must be stored in the database (so that they can be accessed by the SysAdmin)
- Schedule
    - Job microservice must be able to request a job to be scheduled
    - Schedule must be able to determine whether a given job can be scheduled
    - Schedule must assign a job to a particular faculty (or to the free pool, or both) or reject it based on data from the Clusters microservice
- Clusters
    - Users must be able to add a new node
    - FacultyAccounts must be able to free the cluster’s resources for specified days
    - Clusters must contain information about the particular faculty (free pool) and be able to share it with other microservices (if requested)


# Should haves:

- User
    - A user should be able to change their faculty
    - Users should be able to make multiple outstanding requests
- Job
    - Users should be able to remove their jobs
- Schedule
    - Job microservice should be able to unschedule a job
- Clusters
    - Users should be able to remove a node


# Could haves:

- User
- Job
- Schedule
    - Scheduler could be able to reschedule jobs greedily when new ones come in
- Clusters


# Wont-haves:

- User
    - An employee won't be able to approve jobs
- Job
    - Jobs will not be scheduled across several days
- Schedule
    - Scheduler won't be able to finish a Job by distributing the workload for one Job over multiple days
- Clusters

