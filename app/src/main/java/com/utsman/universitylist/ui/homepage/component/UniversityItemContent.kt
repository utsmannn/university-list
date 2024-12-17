package com.utsman.universitylist.ui.homepage.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.utsman.universitylist.data.University

@Composable
fun UniversityItemContent(
    modifier: Modifier,
    university: University,
    onClick: (University) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .then(modifier)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                onClick.invoke(university)
            }
            .background(color = MaterialTheme.colorScheme.primaryContainer)
    ) {
        val (image, title, domain) = createRefs()

        AsyncImage(
            model = university.imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .constrainAs(image) {
                    top.linkTo(parent.top, margin = 12.dp)
                    start.linkTo(parent.start, margin = 12.dp)
                    bottom.linkTo(parent.bottom, margin = 12.dp)
                }
        )

        Text(
            text = university.name,
            maxLines = 2,
            style = MaterialTheme.typography.bodyLarge,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(title) {
                    top.linkTo(image.top)
                    start.linkTo(image.end, margin = 12.dp)
                },
            fontWeight = FontWeight.Black
        )

        Text(
            text = university.domain,
            maxLines = 1,
            style = MaterialTheme.typography.bodyMedium,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(domain) {
                    top.linkTo(title.bottom)
                    start.linkTo(image.end, margin = 12.dp)
                }
        )

    }
}