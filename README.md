# Intelligence

This Java program monitors a given crime family.

## How It Works

The program uses AVL Tree data structure to monitor and keep track of the organization’s
structure using the tips from the informants and provide a detailed analysis of this
structure. The members of the crime family are organized according to a criteria named
golden mean score (GMS) devised from attributes such as criminal record, risk-taking, and
reputation. They are organized in a way that members that are closer to the golden mean are
higher up in the hierarchy.

There is always a current boss of the family who resides at the top of the hierarchy. The
boss has a rank of 0 which is the highest rank and every member has a non-zero rank, which
indicates their importance to the family. Rank is determined by their distance to the boss.
For example, if a member’s superior is the direct inferior of the boss, that member has a
rank of 2.

Every family member except the boss has a direct higher ranking member that gives orders
to them and every member has at most two direct lower-ranking members to give orders to.
If there are two lower-ranking members under the command of a higher-ranking member, one
must have a higher GMS than the commander’s GMS, and one must have a lower GMS than the
commander’s GMS.

Every new member initially joins the family at the bottom of the hierarchy and no two members
have the same GMS. Also, the family ensures that among the members that have no members to
command, the highest ranking and the lowest ranking should not have more than 1 rank
difference. This is ensured by a reorganization process inside the organization.

There may be several possible changes in the organization of the crime family such as a new
member joining or an old member leaving. In addition to this, the Service may request an
analysis of the state of the family before certain operations. These are: 

- Targeting the family (if The Service is planning simultaneous arrests of two family members,
they will need to sabotage their communication with the boss. To do this, they will ask who
the lowest ranking member that is the superior of both of these members).
- Dividing the family (If an operation tries to divide the whole family, The Service will
request the maximum number of independent members that can be targeted).
- Monitoring Ranks in the Family (Find the members with the same rank as a given member)

### Prerequisites

- An IDE or text editor to run the Java code.

## Running the tests

The program takes 2 arguments: name of the input file containing the name of the boss and
changes / operations asked by the Service and a name for the output file.

Some example input and output cases are provided in the repository.